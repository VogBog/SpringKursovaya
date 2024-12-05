const list = document.getElementById('eventsList');
const itemStatus = sessionStorage.getItem('status');

async function subscribe(id) {
    const userId = sessionStorage.getItem('user');
    const token = sessionStorage.getItem('jwt');

    const obj = {
        userId: Number(userId),
        eventId: id
    }
    
    let url = 'http://localhost:8080/secured/events/subscribe/' + id;
    let mtd = 'POST';
    if(itemStatus > -1) {
        url = 'http://localhost:8080/secured/events/unsubscribe?userId='+userId+'&eventId='+id;
        mtd = 'DELETE';
    }

    const response = await fetch(url, {
        method: mtd,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(obj)
    });
    if(response.ok) {
        location.reload();
    }
}

async function init() {
    const token = sessionStorage.getItem('jwt');
    const userId = sessionStorage.getItem('user');
    let url = 'http://localhost:8080/secured/events/notConnected/';

    if(itemStatus > -1) {
        url = 'http://localhost:8080/secured/events/subscribed/';
    }

    const response = await fetch(url + userId, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });

    const rawEvents = await response.json();
    const totalResponse = await fetch('http://localhost:8080/secured/events/total', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(rawEvents)
    });
    const totalEvents = await totalResponse.json();
    let txt = "Купить билет";
    if(itemStatus > -1) {
        txt = "Вернуть билет";
    }
    for (const event of totalEvents) {
        addEvent(list, event, "" +
            "<button onclick='{subscribe("+event.event.id+")}'>"+txt+"</button>"
        );
    }
}

init();