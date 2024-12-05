const list = document.getElementById('locations-list');
const locationName = document.getElementById('location-name');
const eventDate = document.getElementById('date');
const openTime = document.getElementById('open_time');
const closeTime = document.getElementById('close_time');
const errorText = document.getElementById('error_text');

var locationId = -1;

async function submit() {
    if(eventDate.value.length == 0 || openTime.value.length == 0 || closeTime.value.length == 0
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
        dateTime: eventDate.value,
        startTime: openTime.value,
        endTime: closeTime.value,
        locationId: locationId,
        owner: userId,
        approved: 'false'
    }

    const response = await fetch('http://localhost:8080/secured/locationRequest/', {
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
    location.replace('requests.html');
}

function addLocation(id, name) {
    locationName.innerText = name;
    locationId = id;
}

async function init() {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locations', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    const locations = await response.json();
    const user = sessionStorage.getItem('user');
    for (const location of locations) {
        if(location.owner === user)
            continue;
        list.insertAdjacentHTML('beforeend', '<button class="loc" onclick="{addLocation('+location.id+', \''+location.name+'\')}">'+location.name+'</button>');
    }
}

init();