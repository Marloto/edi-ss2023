const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 7071 });

wss.on('connection', (ws, req) => {
    const path = req.url;
    console.log('Client connected: ' + path);
    
    
    ws.on('message', (message) => {
        ws.send("Test Nachricht: " + message);
    });

    ws.on("close", () => {
        
    });
});