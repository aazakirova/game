require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: common.js
    module = sys.zb-common

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
    
    state: CatchAll   
        event!: NoMatch || noContext = true
        random:
            a: Прости, я не понимаю. Давай играть?
            a: Извини, я не знаю, что тебе ответить. Сыграем в игру?
            a: Кажется, я не понимаю, извини. Я хочу сыграть с тобой! 
        go!: {{$session.lastState}}    
            
    state: NormalButtons
        intent!: /Играть
        buttons:
            "Начнем!" -> /Guess
            "Я не знаю правила :(" -> /Rules
            "Отмена" -> /Welcome
        go: /Guess
    
    state: Rules
        a: Я загадаю 4-значное число с неповторяющимися цифрами. Ты должен угадать число. После каждой попытки я буду называть тебе число "коров" (сколько цифр угадано без совпадения с их позициями в тайном числе) и "быков" (сколько цифр угадано вплоть до позиции в тайном числе). Теперь играем?
        go: /Rules/Agree/
    
        state: Agree
        
            state:Yes
            intent: /Согласие
            go: /Guess    
        
            state: No
            intent: /Несогласие
            go: /Welcome
        

    state: Guess
        random:
            a: Попробуй угадать число, которое я загадал!
            a: Угадывай!
            a: Сможешь догадаться? 
        go: /Game    
    
    state: Game
        script:
            $session.number = $jsapi.random(9999) + 1;
            $reactions.answer("Загадано {{$session.number}}");
            $reactions.transition("/Check");
        
    state: Check
        intent: /Число
        script:
            var num = $parseTree._Number;        
            if (num == $session.number) {
                $reactions.answer("У тебя получилось!");
                $reactions.transition("/Rules/Agree");
            }
#            else
#                if (num < $session.number)
#                    $reactions.answer(selectRandomArg(["Мое число больше!", "Бери выше", "Попробуй число больше"]));
#                else $reactions.answer(selectRandomArg(["Мое число меньше!", "Подсказка: число меньше", "Дам тебе еще одну попытку! Мое число меньше."]));

        state: LocalCatchAll
            event: noMatch
            a: Пожалуйста, напиши 4-значное число с неповторяющимися цифрами.
            go!: ..  

    state: OneMore
        a: Новый раунд? 
        go: /OneMoreButton
                
    
    state: OneMoreButton
        buttons:
            "Играть ещё" -> /Guess
            "Отмена" -> /GoodBye
    
    state: GoodBye
        random:
            a: Было здорово! До встречи :)
            a: Уже хочу сыграть снова! 
            a: Возвращайся скорее. Сыграем еще раз!
        intent: /Пока
            





    
  
#    state: activityType || modal = true
#        q!: * $activity *
#        script:
#            log(toPrettyString($parseTree));
#            $session.type = $parseTree._activity;
#        go!: /activityType/greatChoice
        

        
#        state: greatChoice
#            a: Great choice! Let's see what {{$session.type}} activity I can offer you.                    
#           script: 
#                $temp.task = getActivity($session.type);
#            if: $temp.task 
#                random:
#                    a: Look what idea I've found for you! {{$temp.task.activity}}. I hope you like it!
#                    a: {{$temp.task.activity}}. What do you think? Cool activity, isn't it?                         
#                    a: {{$temp.task.activity}}. What an idea! 
#                go!: /activityType/Satisfaction
#            
#        state: Satisfaction
#            buttons:
#                "Another activity!" -> /Ask
#                "Cool, thanks!" -> /GoodBye
#              
#    
#    