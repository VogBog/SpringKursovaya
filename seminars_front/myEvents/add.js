const list = document.getElementById('locations-list');
const locationName = document.getElementById('location-name');
const eventName = document.getElementById('name');
const eventDate = document.getElementById('date');
const openTime = document.getElementById('open_time');
const closeTime = document.getElementById('close_time');
const errorText = document.getElementById('error_text');
const ticketCost = document.getElementById('cost');

const itemStatus = sessionStorage.getItem('status');

var locationId = -1;

function setLocation(locId, locName) {
    locationId = locId;
    locationName.innerText = locName;
}

async function submit() {
    if(eventName.value.length == 0 || eventName.value.length >= 60 ||
        eventDate.value.length == 0 || openTime.value.length == 0 || closeTime.value.length == 0
    ) {
        errorText.innerText = "Некорректные данные!";
        return;
    }
    if(locationId == -1) {
        errorText.innerText = "Не выбрана локация";
        return;
    }
    errorText.innerText = "";

    const token = sessionStorage.getItem('jwt');
    const userId = sessionStorage.getItem('user');
    const obj = {
        name: eventName.value,
        dateTime: eventDate.value,
        startTime: openTime.value,
        endTime: closeTime.value,
        ticketCost: ticketCost.value,
        locationId: locationId,
        owner: userId
    }

    let method = 'POST'
    if(itemStatus != -1) {
        method = 'PUT';
        obj.id = itemStatus;
    }

    const response = await fetch('http://localhost:8080/secured/events', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(obj)
    })
    if(!response.ok) {
        errorText.innerText = "Что-то пошло не так.";
        return;
    }
    location.replace('events.html');
}

async function loadEvent(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/events/' + id, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    if(response.ok) {
        const obj = await response.json();

        eventName.value = obj.name;
        eventDate.value = obj.date_time;
        openTime.value = obj.start_time;
        closeTime.value = obj.end_time;
        ticketCost.value = obj.ticket_cost;
        locationId = obj.location_id;
        locationName.innerText = "Сохраненная локация";
    }
}

async function init() {
    if(itemStatus != -1) {
        loadEvent(itemStatus);
    }

    const userId = sessionStorage.getItem('user');
    const token = sessionStorage.getItem('jwt');
    if(userId == null || token == null) {
        location.replace('../index.html');
        return;
    }

    const response = await fetch('http://localhost:8080/secured/locations/ofUser/' + userId, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    if(!response.ok) {
        location.replace('../index.html');
        return;
    }
    const locations = await response.json();
    for (const location of locations) {
        list.insertAdjacentHTML("beforeend", "" +
            "<button onclick='{setLocation("+location.id+",\""+location.name+"\")}'>"+location.name+"</button>"
        );
    }
}

init();