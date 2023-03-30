const client = require('mqtt').connect(process.env.MQTT_HOST || 'mqtt://localhost:1883');
client.on('connect', () =>
    setInterval(() => {
        const v = Math.random();
        client.publish(`test/rnd`, `${Math.random()}`)
        console.log(`Send ${v} to test/rnd`);
    }, parseInt(process.env.FREQUENCY) || 1000))