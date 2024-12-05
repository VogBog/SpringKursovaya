const list = document.getElementById('events-list');

async function approve(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locationRequest/approve/' + id, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });

    if(response.ok) {
        location.reload();
    }
}

async function deny(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locationRequest/deny/' + id, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });

    if(response.ok) {
        location.reload();
    }
}

async function init() {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locationRequest/toMeForApprove', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    const json = await response.json();
    for (const request of json) {
        const response2 = await fetch('http://localhost:8080/secured/users/' + request.owner, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
        const owner = await response2.json();

        addRequest(list, request, "<span>От: "+owner.name+"</span>"+
            "<button onclick='{approve("+request.id+")}'>Принять</button>" +
            "<button onclick='{deny("+request.id+")}'>Отклонить</button>"
            , false);
    }
}

init()