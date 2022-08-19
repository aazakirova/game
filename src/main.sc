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
    
    state: CatchAll || noContext = true
        event!: NoMatch
        random:
            a: Прости, я не понимаю. Давай играть?
            a: Извини, я не знаю, что тебе ответить. Сыграем в игру?
            a: Кажется, я не понимаю, извини. Я хочу сыграть с тобой! 
        go!: {{$session.lastState}}    
            
    state: NormalButtons
        buttons:
            "Начнем!" -> /Guess
            "Я не знаю правила" -> /Rules
            "Отмена" -> /Welcome
        go: /Guess
    
    state: Rules
        a: Я загадаю 4-значное число с неповторяющимися цифрами. Ты должен угадать его. После каждой попытки я буду называть тебе число "коров" (сколько цифр угадано без совпадения с их позициями в тайном числе) и "быков" (сколько цифр угадано вплоть до позиции в тайном числе). Теперь играем?
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
            a: Сможешь угадать число? Введи его.
        intent: /Попытка
        go: /Game    
    
    
    
    
    
    state: Game
        script:
            var bulls, cows;
            function Random(a, b)
                {
                    return Math.floor(Math.random()*10);
                }
            function Guess()
            {
               for (var i=0; i<4;i++) 
               {
                   do 
                   {
                       var c=Random(0,9);
                   }
                   while(s.indexOf(c)>=0) 
                   s=s+c;
               }       
               return s;
            }
            function Analize(make,try)
            {
                bulls=0;
                cows=0;
                for(var i=0; i<4;i++)
                       {
                           if (make[i]==try[i])
                               bulls++;
                           else:
                               if(make.indexOf(try[i])>=0) cows++; 
                        }
            }
            var g = Guess();
            for(var i=o,i<10;i++)
            {
                var num
                Analize(g, num);
                var s = "Твоё число: " + c + "." + "Быки: " + bulls+ "Коровы: " + cows 
                alert(s);
                if (g==m) 
                {
                    alert("Вы выиграли!");
                    break;
                }
            }
 
 
 
 
 
        
    state: Check
        intent: /Число
        script:
            var num = $parseTree._Number;        
            if (num == $session.number) {
                $reactions.answer("У тебя получилось!");
                $reactions.transition("/OneMore");
            }
#            else
           
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
            





# console.log(countBullsAndCows($session.number, num))


    
  
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
#                    a: «Коровы»: {}, "Быки": {}.
#                go!: /activityType/Satisfaction
#               