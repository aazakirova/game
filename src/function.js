//function countBullsAndCows(secret, suggestition) {
//  let bulls = 0, cows = 0;
//  (suggestition + '').split('').forEach((n, i) =>
//    (secret + '')[i] === n ? bulls++ : ~(secret + '').indexOf(n) && cows++);
//  return [bulls, cows];
//};
    

///

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