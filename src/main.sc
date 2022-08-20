require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: common.js
    module = sys.zb-common
    
require: function.js
    
init:
    bind("postProcess", function($context) {
        $context.session.lastState = $context.currentState;
    })
    
theme: /

    state: Welcome
        q!: $regex</start>
        intent: /Привет
        a:  Привет! Сыграем? Надеюсь, ты знаешь правила игры "Быки и коровы" ;)
        script:
            $jsapi.startSession()
        go!: /NormalButtons
    
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Прости, я не понимаю тебя. Давай играть?
            a: Извини, я не знаю, что тебе ответить. Сыграем в игру?
            a: Кажется, я не понимаю, извини. Я хочу сыграть с тобой! 
        go!: {{$session.lastState}} 
            
    state: NormalButtons
        buttons:
            "Начать" -> /Guess
            "Я не знаю правила" -> /Rules
            "Отмена" -> /Welcome
        go: /Guess
    
    state: Rules
        a: Я загадаю 4-значное число с неповторяющимися цифрами. Ты должен угадать его. После каждой попытки я буду называть тебе число "коров" (сколько цифр угадано без совпадения с их позициями в тайном числе) и "быков" (сколько цифр угадано вплоть до позиции в тайном числе). Теперь играем?
        go: /Rules/Agree
        
        state: Agree
        
            state: Yes
                intent: /Согласие
                go!: /Guess
        
            state: No
                intent!: /Несогласие
                go!: /Welcome
                
    state: Guess
        random:
            a: Попробуй угадать число, которое я загадал!
            a: Угадывай!
            a: Сможешь угадать число? Введи его.
        # вызываем функцию, генерирующую случайное число и переходим в стейт /Check
        script:
            $session.number = GetSecretNumber(4);
            # для проверки верности выполнения задачи при неободимости:
            $reactions.answer("Загадано {{$session.number}}"); 
            
    state: Check
        intent: /Попытка
        script:
            if ($parseTree._Number && $session.number) {
                // сохраняем введенное пользователем число и сгенерированное число в виде строки
                var num = $parseTree._Number.toString();
                var secret = $session.number.toString();
                // проверяем, угадал ли пользователь загаданное число и выводим соответствующую реакцию
                if (num == secret) {
                    $reactions.answer("Ты выиграл!");
                    $reactions.transition("/Rules/Agree");
                    $reactions.transition("/OneMore");
                }
                else {
                    if (num.length != secret.length) {
                        $reactions.answer("Введи 4-значное число с неповторяющимися цифрами.");
                    }
                    else {
                        $reactions.answer(Check(secret, num));
                    }
                }
            }
            
    state: OneMore
        a: Новый раунд? 
        q: buttons
        buttons:
            "Играть ещё" -> /Guess
            "Отмена" -> /GoodBye
    
    state: GoodBye
        random:
            a: Было здорово! До встречи :)
            a: Уже хочу сыграть снова! 
            a: Возвращайся скорее. Сыграем еще раз!
        intent!: /Пока
        go!: /Welcome