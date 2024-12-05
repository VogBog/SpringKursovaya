const username = document.getElementById('username');
const login = document.getElementById('login');

function quit() {
    location.replace('../index.html');
}

function goTo(path) {
    location.replace(path)
}

function goToAllEvents(status) {
    sessionStorage.setItem('status', status);
    location.replace('../allEvents/events.html');
}

async function init() {
    const myId = sessionStorage.getItem('user');
    console.log(myId);
    const myToken = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/users/' + myId, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + myToken
        }}
    );
    if(!response.ok) {
        quit();
        return;
    }

    const json = await response.json();
    
    login.innerText = json.login;
    username.innerText = json.name;
}

init();