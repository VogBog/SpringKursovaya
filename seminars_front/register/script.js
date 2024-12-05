const username = document.getElementById('name');
const login = document.getElementById('login');
const password = document.getElementById('password');
const errorText = document.getElementById('error-text');

async function tryToRegister() {
    if(username.value.length == 0 || username.value.length >= 60 ||
        login.value.length == 0 || login.value.length >= 60 ||
        password.value.length == 0 || password.value.length >= 60
    ) {
        errorText.innerText = "Некорректные данные!";
        return;
    }
    errorText.innerText = "";

    const existsResponse = await fetch('http://localhost:8080/auth/controller/userExists/' + login.value);
    if(!existsResponse.ok) {
        return;
    }
    const existsValue = await existsResponse.json();
    if(existsValue.value) {
        errorText.innerText = "Такой пользователь уже существует!";
        return;
    }

    const obj = {
        username: username.value,
        login: login.value,
        password: password.value
    }

    const registerResponse = await fetch('http://localhost:8080/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(obj)
    });
    if(!registerResponse.ok) {
        errorText.innerText = "Регистрация не получилась!";
        return;
    }
    location.replace('../index.html');
}