var socket;
if (!window.WebSocket) {
	window.WebSocket = window.MozWebSocket;
}
if (window.WebSocket) {
	socket = new WebSocket("ws://localhost:8080/ws");
	socket.onmessage = function(event) {
	   handle(event);
	};
	socket.onopen = function(event) {
		
	};
	socket.onclose = function(event) {
		 
	};
} else {
	alert("你的浏览器不支持 WebSocket！");
}
