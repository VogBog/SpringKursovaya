const login = document.getElementById('login');
const password = document.getElementById('password');
const errorText = document.getElementById('error-text');

async function isUserExists(login) {
    const response = await fetch('http://localhost:8080/auth/controller/userExists/' + login);

    const json = await response.json();
    return json.value;
}

async function tryToLogin() {
    if(login.value.length == 0 || login.value.length >= 60) {
        errorText.innerText = "Ошибка! Логин некорректный!";
        return;
    }
    if(password.value.length == 0 || password.value.length >= 60) {
        errorText.innerText = "Ошибка! Пароль некорректный!";
        return;
    }
    errorText.innerText = "";

    const isExists = await isUserExists(login.value);
    if(!isExists) {
        errorText.innerText = "Ошибка! Такого пользователя не существует!";
        return;
    }

    const paramObject = {
        login: login.value,
        password: password.value
    }
    const response = await fetch('http://localhost:8080/auth/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paramObject)
    });
    if(response.status === 401) {
        errorText.innerText = "Неверный пароль!";
        return;
    }
    if(!response.ok) {
        errorText.innerText = "ЧТо-то пошло не так. Возможно, сервер не отвечает.";
        return;
    }

    const text = await response.text();
    sessionStorage.setItem('jwt', text);

    const userResponse = await fetch("http://localhost:8080/secured/users/byLogin/" + login.value, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + text
        }
    });
    if(!userResponse.ok) {
        errorText.innerText = "Что-то пошло не так. Возможно, проблема на стороне сервера.";
        return;
    }
    const user = await userResponse.json();
    sessionStorage.setItem('user', user.id);
    location.replace('user_profile/profile.html');
}