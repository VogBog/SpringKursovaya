const eventName = document.getElementById('name');
const eventCost = document.getElementById('cost');
const errorText = document.getElementById('error_text');

const itemState = sessionStorage.getItem('status');

async function submit() {
    errorText.innerText = "";
    if(eventName.value.length == 0 || eventName.value.length >= 60 ||
        eventCost.value.length == 0 || eventCost.value.length > 5
    ) {
        errorText.innerText = "Некорректные данные!";
        return;
    }

    const token = sessionStorage.getItem('jwt');
    const reqRes = await fetch('http://localhost:8080/secured/locationRequest/' + itemState, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    const reqJson = await reqRes.json();

    let obj = {
        name: eventName.value,
        dateTime: reqJson.dateTime,
        startTime: reqJson.startTime,
        endTime: reqJson.endTime,
        ticketCost: eventCost.value,
        peoplesCount: 0,
        locationId: reqJson.locationId,
        owner: reqJson.owner
    }

    const response = await fetch('http://localhost:8080/secured/events/uploadFromRequest/' + itemState, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(obj)
    })
    if(!response.ok) {
        errorText.innerText = "Не получилось";
        return;
    }
    location.replace('requests.html');
}