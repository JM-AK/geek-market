var stompClient = null;

window.onload = connect();

function connect() {
    var socket = new SockJS('/hello_cart');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/add_product_to_cart', function(greetingAddToCart){
            showGreeting(JSON.parse(greetingAddToCart.body).content);
        });
    });
}

function sendContent() {
    var name = 'товар';
    stompClient.send("/app/hello_cart", {}, JSON.stringify({ 'name': name }));
}

function showGreeting(message) {
    console.log(message);
    document.getElementById("resultCartQuantityInput").value=message;
}