var NotificationChannel = {
    /**
     * WebSocket instance.
     * 
     * @type WebSocket
     */
    ws: undefined,
    /**
     * @description Initializes message channel
     */
    init: function (channelServer) {
    	NotificationChannel.ws = new ReconnectingWebSocket(channelServer);
        NotificationChannel.ws.reconnectInterval = 10000;

        NotificationChannel.ws.onopen = function () {
            setInterval(function () {
            	NotificationChannel.ws.send('-hb-');
            }, 1000 * 60 * 3);
        };

        NotificationChannel.ws.onmessage = function (evt) {
            var data = JSON.parse(evt.data);
            alert(data);
        };

        NotificationChannel.ws.onclose = function () {
        	NotificationChannel.ws.close();
        };

        NotificationChannel.ws.onerror = function (err) {
            console.log("ERROR", err);
        };
    }
};