const express = require('express')
const app = express()
const port = 3000

const data = [];

function fill() {
    const now = Math.floor(new Date().getTime() / 1000);
    const start = data.length ? data[data.length - 1] : {time: (now - 60), humidity: 0.5, temperature: 20};
    let last = start;
    for(let i = start.time + 1; i < now; i ++) {
        data.push({time: i, 
            humidity: last.humidity + (Math.random() * 0.02 - 0.01),
            temperature: last.temperature + (Math.random() * 0.2 - 0.1)})
        last = data[data.length - 1];
    }
}
fill();

app.get('/humidity', (req, res) => {
    fill();
    const now = Math.floor(new Date().getTime() / 1000);
    res.send(data.filter(el => el.time > now - 10).map(el => ({time: el.time, value: el.humidity})));
})

app.get('/temperature', (req, res) => {
    fill();
    const now = Math.floor(new Date().getTime() / 1000);
    res.send(data.filter(el => el.time > now - 10).map(el => ({time: el.time, value: el.temperature})));
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})