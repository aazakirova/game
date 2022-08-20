function Random(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

// генерируем четырехзначное число без повторяющихся цифр
function GetSecretNumber(length) {
    var s = '';
    s += Random(1, 10); // учитываем, что первая цифра не 0
    for (var i = 1; i < length; i++) {
        do {
            var c = Random(0, 10);
        }
        while(s.indexOf(c) >= 0)
        s += c;
    }
    return Number(s);
}

// проверяем соответсвие условию
function Check(secret, attempt) {
    var bulls = 0; 
    var cows = 0;
    for (var i = 0; i < attempt.length; i++) {
        if (secret[i] == attempt[i]) {
            bulls++;        }
        else {
            if (secret.indexOf(attempt[i]) >= 0) {
                cows++;
            }
        }
            
    }
    var s = "Твоё число: " + attempt + ". " + "Быки: " + bulls + ". " + "Коровы: " + cows + "."
    return s;
}  