var stompClient = null;

window.onload = connect();

function setConversation(connected) {
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#cart_quantity_greetings").html("");
}


function connect() {
    var socket = new SockJS('/cart_websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConversation(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/add_to_cart', function(greetingAddToCart){
            showGreeting(JSON.parse(greetingAddToCart.body).content);
        });
    });
}

function sendCartQuantity() {
    var name = 'товар';
    stompClient.send("/app/hello_cart", {}, JSON.stringify({'name': name}));
}

function showGreeting(message) {
    console.log(message);
    document.getElementById("cart_quantity_greetings").innerText=message;
}


