const client = require('mqtt').connect(process.env.MQTT_HOST || 'mqtt://localhost:1883');

const DEFAULT_STOCK = parseInt(process.env.DEFAULT_STOCK) || 100;
const DEFAULT_RESTOCK = parseInt(process.env.DEFAULT_STOCK) || 10;
const articles = {
    "433fe831-3401-4e28-b550-4b6efa425431": {frequency: 10000, stock: DEFAULT_STOCK, restock: DEFAULT_RESTOCK, target: DEFAULT_STOCK},
    "603c8cde-f5d6-4cc2-89ea-aa97276d33cf": {frequency: 10000, stock: DEFAULT_STOCK, restock: DEFAULT_RESTOCK, target: DEFAULT_STOCK},
    "0aee9302-bcc8-4dcc-9e4b-90cc984dc2fe": {frequency: 10000, stock: DEFAULT_STOCK, restock: DEFAULT_RESTOCK, target: DEFAULT_STOCK},
    "2906a051-3c5f-4032-98e7-b5c4b45a1573": {frequency: 10000, stock: DEFAULT_STOCK, restock: DEFAULT_RESTOCK, target: DEFAULT_STOCK},
    "1a88e23c-56ce-4a17-8b5b-4d80df805792": {frequency: 10000, stock: DEFAULT_STOCK, restock: DEFAULT_RESTOCK, target: DEFAULT_STOCK},
};

function publish(id, article) {
    client.publish(`article/${id}/stock`, `${article.stock}`);
}

client.on('connect', () => 
    Object.keys(articles).forEach(article => {
        client.subscribe(`article/${article}/picked`,
            (err) => err && console.log("Error while subscribing to test"));
        setInterval(() => {
            articles[article].stock = Math.min(articles[article].stock + articles[article].restock, articles[article].target);
            publish(article, articles[article]);
        }, articles[article].frequency);
    }))

client.on('message', (topic, message) => {
    const id = /^article\/(.*?)\/picked$/.exec(topic)[1];
    console.log(`Got message for ${topic} with ${message}`)
    if(articles[id]) {
        articles[id].stock -= parseInt(message) || 1;
        publish(id, articles[id]);
    }
})
