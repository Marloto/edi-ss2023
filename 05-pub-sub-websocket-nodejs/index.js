const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 7071 });
const ChannelManager = require('./manager');

const manager = new ChannelManager();

wss.on('connection', (ws, req) => {
    const path = req.url;
    console.log('Client connected: ' + path);
    
    const subscription = manager.subscribe(path, message => ws.send(message));
    
    ws.on('message', (message) => {
        const channel = subscription.channel;
        console.log('Received message from ' + channel.topic + ': ' + message);
        channel.notify(message);
    });

    ws.on("close", () => {
        subscription.unsubscribe();
    });
});