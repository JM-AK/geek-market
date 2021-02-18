var stompClient = null;

window.onload = connect();

function setConnected(connected) {
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
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/add_to_cart', function(greetingAddToCart){
            showGreeting(JSON.parse(greetingAddToCart.body).content);
        });
    });
}

function sendCartQuantity() {
    var quantity = "cart_count";
    stompClient.send("/app/hello_cart", {}, JSON.stringify({'name': quantity}));
}

function showGreeting(message) {
    console.log(message);
    document.getElementById("cart_quantity_greetings").innerText=message;
    // $("#cart_quantity_greetings").append("<tr><td>" + message + "</td></tr>");
}


