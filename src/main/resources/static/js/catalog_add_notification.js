var stompClient = null;

window.onload = connect();

function setConversation(connected) {
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#shop_greetings").html("");
}

function connect() {
    var socket = new SockJS('/cart_websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConversation(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/add_to_catalog', function(greetingAddToCatalog){
            showGreeting(JSON.parse(greetingAddToCatalog.body).content);
        });
    });
}

function showGreeting(message) {
    console.log(message);
    document.getElementById("shop_greetings").innerText=message;
}


