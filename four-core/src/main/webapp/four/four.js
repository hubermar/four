// module Four

var Four = (function() {

	// private
	const SERVER_URL = "ws://localhost:3333/four/api/events";
	
	const COIN_SIZE = 100;
	const COIN_HALF_SIZE = COIN_SIZE / 2;

	const EVENT_CONFIG = 'LOADED_BOARD';
	const EVENT_DRAW   = 'INSERTED_COIN';
	const EVENT_NEXT   = 'NEXT_PLAYER';

	const PLAYER_ONE = 'ONE';
	const PLAYER_TWO = 'TWO';

	var canvas;
	var width;
	var height;
	var currentPlayer;

	var socket;
	
	var init = function() {
		initCanvas();
		socket = new WebSocket(SERVER_URL);
		socket.onmessage = onMessage;
		socket.onerror = function (error) {
		  console.log('WebSocket Error ' + error);
		};
		socket.onopen = function () {
			var event = {
				type : 'GET_BOARD'
			};
			fireEvent(event);
		};
	}
	
	function onMessage(message) {
		console.debug(message);
		var event = JSON.parse(message.data);
		
		switch (event.type) {
		case EVENT_DRAW:
			drawCoin(event.payload['COLUMN'], event.payload['ROW'], event.payload['PLAYER']);
			break;
		case EVENT_NEXT:
			currentPlayer = event.payload['PLAYER'];
			break;
		case EVENT_CONFIG:
			currentPlayer = event.payload['PLAYER'];
			width = event.payload['COLUMN'];
			height = event.payload['ROW'];
			drawBoard();
			break;
		case undefined:
			break;
		default:
			console.log('unknown event type=' + event.type);
		}
	}

	function initCanvas() {
		canvas = document.getElementById('board');
		canvas.addEventListener("click", function(evt) {
			var pos = getMousePos(evt);
			console.debug(getMousePos(evt));
			var col = Math.floor(pos.x / COIN_SIZE);
			console.debug(col);
			var event = {
				type : 'INSERT_COIN',
				payload: {
					'COLUMN' : col,
					'PLAYER' : currentPlayer					
				}
			};
			fireEvent(event);
		});
	}

	function fireEvent(event) {
		socket.send(JSON.stringify(event));
//		return new Promise(function(resolve, reject) {
//			var req = new XMLHttpRequest();
//			req.open('POST', BASE_URI + '/events/');
//			req.setRequestHeader('Content-Type', 'application/json');
//			req.onload = function() {
//				console.log('firing event=' + event);
//				if (req.status === 204) {
//					// console.log('event successful');
//					// resolve(req.response);
//				} else {
//					reject(Error(req.statusText));
//				}
//			};
//
//			req.onerror = function() {
//				reject(Error("Network Error"));
//			};
//			console.log('sending ' + JSON.stringify(event));
//			req.send(JSON.stringify(event));
//		});
	}

	var drawBoard = function() {
		canvas.height = COIN_SIZE * height;
		canvas.width = COIN_SIZE * width;
		var ctx = canvas.getContext('2d');
		if (ctx) {
			// draw board square
			ctx.clearRect(0, 0, COIN_SIZE * width, COIN_SIZE * height);

			for (var col = 0; col <= width; col++) {
				// grid
				var gx = col * COIN_SIZE;
				ctx.lineWidth = 1;
				ctx.strokeStyle = getColor(currentPlayer);
				ctx.beginPath();
				ctx.moveTo(gx, 0);
				ctx.lineTo(gx, height * COIN_SIZE);
				ctx.stroke();
			}
		}
	}

	function drawCoin(column, row, player) {
		var ctx = canvas.getContext('2d');
		if (ctx) {
			var gx = column * COIN_SIZE;
			var gy = (height - row - 1) * COIN_SIZE;
			ctx.fillStyle = getColor(player);
			var cx = gx + COIN_HALF_SIZE;
			var cy = gy + COIN_HALF_SIZE
			ctx.beginPath();
			ctx.arc(cx, cy, COIN_HALF_SIZE, 0, 2 * Math.PI);
			ctx.fill();
		}
	}

	function getColor(player) {
		if (player === 'ONE') {
			return "rgba(255, 0, 0, 1.0)";
		} else if (player === 'TWO') {
			return "rgba(255, 255, 0, 1.0)";
		} else {
			return "rgba(0, 0, 0, 0.1)";
		}
	}

	function getMousePos(evt) {
		var rect = canvas.getBoundingClientRect();
		return {
			x : evt.clientX - rect.left,
			y : evt.clientY - rect.top
		};
	}

	// public
	return {
		init : init
	}
})();

document.addEventListener("DOMContentLoaded", function(event) {
	Four.init();
});