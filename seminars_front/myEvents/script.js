const eventsList = document.getElementById('events-list')

function goToAdd() {
    sessionStorage.setItem('status', -1);
    location.replace('add.html');
}

function goToEdit(id) {
    sessionStorage.setItem('status', id);
    location.replace('add.html');
}

async function deleteBy(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/events/' + id, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    if(response.ok) {
        location.reload();
    }
}

async function init() {
    const userId = sessionStorage.getItem('user');
    const token = sessionStorage.getItem('jwt');
    const eventResponse = await fetch('http://localhost:8080/secured/events/byOwner/' + userId, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    if(!eventResponse.ok) {
        location.replace('../user_profile/profile.html');
        return;
    }
    const events = await eventResponse.json();

    const totalResponse = await fetch('http://localhost:8080/secured/events/total', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(events)
    })
    const totals = await totalResponse.json();

    for (const event of totals) {
        addEvent(eventsList, event, "" +
            "<button onclick='{goToEdit("+event.event.id+")}'>Редактировать</button>" +
            "<button onclick='{deleteBy("+event.event.id+")}'>Удалить</button>"
        );
    }
}

init();