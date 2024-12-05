function goTo(path) {
    location.replace(path);
}

function addLocation(obj, json, extraHtml) {
    let txt = "<div class='location'>" +
    "<span>" + json.name + "</span>" +
    "<span>Открыто с " + json.openTime + " до " + json.closeTime + "</span>" +
    "Максимальная вместимость: " + json.maxPeople;
    if(extraHtml != null) {
        txt += extraHtml;
    }
    txt += "</div>";
    obj.insertAdjacentHTML("beforeend", txt);
}

function addEvent(obj, json, extraHtml) {
    let tickets = "0";
    if(json.event.peoplesCount != null) {
        tickets = json.event.peoplesCount;
    }
    let txt = "<div class='location'>" +
    "<span>" + json.event.name + "</span>" +
    "<span>" + json.event.dateTime + "</span>" +
    "<span>Идёт с "+json.event.startTime+" до "+json.event.endTime+"</span>"+
    "<span>Билет стоит "+json.event.ticketCost+"</span>" +
    "<span>Билетов продано "+tickets+"</span>" +
    "<span>Локация: "+json.location+"</span>";
    if(extraHtml != null) {
        txt += extraHtml;
    }
    txt += "</div>";
    obj.insertAdjacentHTML("beforeend", txt);
}

async function addRequest(obj, json, extraHtml, approved) {
    const token = sessionStorage.getItem('jwt');
    const locationResponse = await fetch('http://localhost:8080/secured/locations/' + json.locationId, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });
    const location = await locationResponse.json();

    let txt = "<div class='location";
    if(approved) {
        txt += " approved";
    }
    txt += "'>" +
    "<span>Локация: "+location.name+"</span>" +
    "<span>Дата: "+json.dateTime+"</span>" +
    "<span>Идёт с "+json.startTime+" по "+json.endTime+"</span>";
    if(extraHtml != null) {
        txt += extraHtml;
    }
    txt += "</div>";
    obj.insertAdjacentHTML("beforeend", txt);
}