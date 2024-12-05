const list = document.getElementById('events-list');

function goToAdd() {
    location.replace('add.html');
}

async function removeRequest(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locationRequest/' + id, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    location.reload();
}

function goToAddEvent(id) {
    sessionStorage.setItem('status', id);
    location.replace('addEvent.html');
}

async function init() {
    const token = sessionStorage.getItem('jwt');

    const response = await fetch('http://localhost:8080/secured/locationRequest/ofUserAll', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    const json = await response.json();
    for (const request of json) {
        let txt = "";
        if(request.approved) {
            txt += '<button onclick="{goToAddEvent('+request.id+')}">Создать мероприятие</button>'
        }

        addRequest(list, request, '<button onclick="{removeRequest('+request.id+')}">Удалить</button>' + txt, request.approved);
    }
}

init();