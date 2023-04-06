const waitPort = require('wait-port');
const os = require('os');
const osUtils = require('os-utils');
const mqtt = require('mqtt');
const {URL} = require('url');
const express = require('express');
const debug = require('debug')('system-info:app');

let client;
let connected = false;
setInterval(function () {
    if (!connected) {
        return;
    }
    osUtils.cpuUsage(function (v) {
        client.publish(`servers/${os.hostname()}/cpu-usage`, `${v}`);
    });
    osUtils.cpuFree(function (v) {
        client.publish(`servers/${os.hostname()}/cpu-free`, `${v}`);
    });
    client.publish(`servers/${os.hostname()}/free-mem`, `${osUtils.freemem()}`);
    client.publish(`servers/${os.hostname()}/process/usage-mem`, `${process.memoryUsage().rss}`);
    client.publish(`servers/${os.hostname()}/process/heap-total`, `${process.memoryUsage().heapTotal}`);
    client.publish(`servers/${os.hostname()}/total-mem`, `${osUtils.totalmem()}`);
    client.publish(`servers/${os.hostname()}/freemem-percentage`, `${osUtils.freememPercentage()}`);
}, parseInt(process.env.FREQUENCY) || 1000);

const mqttHost = process.env.MQTT_HOST || 'mqtt://localhost:1883';
const mqttHostUrl = new URL(mqttHost)
const params = {
    host: mqttHostUrl.hostname,
    port: parseInt(mqttHostUrl.port)
};
debug(`Connect to ${mqttHost}`);

waitPort(params)
    .then(({ open, ipVersion }) => {
        if (open) {
            client = mqtt.connect(mqttHost);
            client.on('connect', function () {
                connected = true;
            })
        }
    })
    .catch((err) => {
        debug(`An unknown error occured while waiting for the port: ${err}`);
    });

const app = express();
const port = process.env.PORT || 3000;

let data = [];
const chars = [..."ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"];

app.get('/', (req, res) => {
    res.send('Hello World!')
});

let working = 0;
app.get('/fill/:amount', (req, res) => {
    const amount = parseInt(req.params.amount) * 1024 * 1024;
    working ++;
    
    
    // create random 1024char strings
    let amountToDo = amount;
    debug(`Fill space with ${req.params.amount} MB, current is ${process.memoryUsage().rss / 1024 / 1024} MB`)
    const createStuff = () => {
        setTimeout(() => {
            debug(`Usage is now ${process.memoryUsage().rss / 1024 / 1024} MB`);
            if(amountToDo <= 0) {
                working --;
                return res.status(204).send();
            }

            let j = 0;
            while(amountToDo >= 0 && j < 256) {
                data.push([...Array(1024)].map(i=>chars[Math.random()*chars.length|0]).join(''));
                amountToDo -= 1024;
                j ++;
            }
            createStuff();
            amountToDo -= 1024;
        });
    }
    createStuff();
    
});

app.listen(port, () => {
    debug(`Example app listening on port ${port}`)
})