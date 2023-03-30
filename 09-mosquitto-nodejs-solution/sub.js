const client = require('mqtt').connect(process.env.MQTT_HOST || 'mqtt://localhost:1883');
client.on('connect', () =>
    client.subscribe(`test/rnd`, 
        (err) => err && console.log("Error while subscribing to test")))
client.on('message', (topic, message) =>
    console.log(topic, message.toString()))