var stompClient = null;

window.onload = connect();

function connect() {
    var socket = new SockJS('/hello_catalog');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/add_product_to_catalog', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function sendName() {
    var name = 'товар';
    stompClient.send("/app/hello_catalog", {}, JSON.stringify({ 'name': name }));
}

function showGreeting(message) {
    console.log(message);
    document.getElementById("resultProductNameInput").value=message;
}