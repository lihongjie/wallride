$(document).ready(function () {
    // Stomp.js boilerplate
    if (location.search == '?ws') {
        var ws = new WebSocket('ws://192.168.31.23:15674/ws');
    } else {
        var ws = new SockJS('http://192.168.31.23:15674/stomp');
    }
//Init Client
    var client = Stomp.over(ws);
// SockJS does not support heart-beat: disable heart-beats
    client.heartbeat.outgoing = 0;
    client.heartbeat.incoming = 0;

// Declare on_connect
    var on_connect = function(x) {
        client.subscribe("/exchange/rabbitmq/lihongjie", function(msg) {
            console.log("message: " + msg.body)
        });
    };

// Declare on_error
    var on_error = function() {
        console.log("error");
    };

// Connect to RabbitMQ
    client.connect('test', '123456', on_connect, on_error, '/');
});

