const locationsParent = document.getElementById('locationsList')

function editLocation(id) {
    sessionStorage.setItem('status', id);
    location.replace('add.html');
}

async function removeLocation(id) {
    const token = sessionStorage.getItem('jwt');
    const response = await fetch('http://localhost:8080/secured/locations/delete/' + id, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    location.reload();
}

function addLocationClicked() {
    sessionStorage.setItem('status', -1);
    location.replace('add.html');
}

function addMyLocation(obj) {
    addLocation(locationsParent, obj,
        "<button onclick=\"{editLocation("+obj.id+");}\">Редактировать</button>" +
        "<button onclick=\"{removeLocation("+obj.id+")}\">Удалить</button>"
    );
}

async function init() {
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
        addMyLocation(location);
    }
}

init();