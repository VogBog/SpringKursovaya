const input_name = document.getElementById('name');
const open_time = document.getElementById('open_time');
const close_time = document.getElementById('close_time');
const max_people = document.getElementById('max_people');
const error_text = document.getElementById('error_text');

const itemStatus = sessionStorage.getItem('status');

async function submit() {
    if(input_name.value.length == 0 || input_name.value.length >= 60) {
        error_text.innerText = "Название некорректное!";
        return;
    }
    if(open_time.value.length == 0 || close_time.value.length == 0) {
        error_text.innerText = "Время некорректное!";
        return;
    }
    if(max_people.value.length == 0 || max_people.value.length > 4) {
        error_text.innerText = "Колчиество мест некорректное!";
        return;
    }
    error_text.innerText = "";
    if(sessionStorage.getItem('user') == null) {
        goTo('../index.html');
    }

    const obj = {
        name: input_name.value,
        openTime: open_time.value,
        closeTime: close_time.value,
        maxPeople: max_people.value,
        owner: sessionStorage.getItem('user')
    }

    console.log(obj);

    let mtd = 'POST';
    let url = 'http://localhost:8080/secured/locations/add'
    if(itemStatus >= 0) {
        obj.id = itemStatus;
        url = 'http://localhost:8080/secured/locations/edit'
        mtd = 'PUT'
    }

    const token = sessionStorage.getItem('jwt');

    const response = await fetch(url, {
        method: mtd,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(obj)
    });

    if(!response.ok) {
        error_text.innerText = "Кажется, ваши данные некорректны!";
        return;
    }

    location.replace('locations.html');
}

async function init() {
    if(itemStatus == -1) {
        return;
    }
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locations/' + itemStatus, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    if(!response.ok) {
        location.replace("locations.html");
    }
    const answer = await response.json();
    input_name.value = answer.name;
    open_time.value = answer.openTime;
    close_time.value = answer.closeTime;
    max_people.value = answer.maxPeople;
}

init();