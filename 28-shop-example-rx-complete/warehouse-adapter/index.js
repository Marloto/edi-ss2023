// Imports
const { Kafka, Partitioners } = require('kafkajs');
const mqtt = require('mqtt');

// Create MQTT Client
const client = mqtt.connect(process.env.MQTT_HOST || 'mqtt://localhost:1883');
client.on('connect', () =>
    client.subscribe(`article/+/stock`,
        (err) => {
            if (err) {
                console.log("Error while subscribing to test")
            } else {
                console.log("Connected successfully to MQTT broker")
            }
        }))
client.on('message', (topic, message) => {
    // Handle stock change in warehouse
    const stockValue = parseInt(message);
    const topicParts = topic.split('/');
    if (topicParts.length < 2) {
        console.error("Missing elements in topic: " + topic);
        return;
    }
    const articleId = topicParts[1];
    // Broadcast new value into topic in kafka
    broadcastNewStockChange(articleId, stockValue);
})

// Create Kafka Producer
const kafka = new Kafka({
    clientId: process.env.KAFKA_CLIENT_ID || 'example-consumer',
    brokers: [process.env.KAFKA_HOST || 'localhost:9092']
})
const changesTopic = process.env.KAFKA_STOCK_CHANGE_TOPIC || 'stock-changes';
const shippingTopic = process.env.KAFKA_SHIPPING_TOPIC || 'shipping';
const producer = kafka.producer({
    createPartitioner:
        Partitioners.DefaultPartitioner
})
const consumer = kafka.consumer({ groupId: 'warehouse-adapter' })

async function broadcastNewStockChange(article, value) {
    if (!kafkaConnected) {
        console.error("Kafka not connected right now");
        return;
    }
    try {
        await producer.send({
            topic: changesTopic,
            messages: [
                {
                    key: article,
                    value: JSON.stringify({ value })
                }]
        })
        console.log(`Published new stock for ${article} with ${value}`);
    } catch (e) {
        console.error(`Error while publishing new stock for ${article} with ${value}: ${e}`);
    }
}

let kafkaConnected = false;
const runProducer = async () => {
    await producer.connect()
    kafkaConnected = true;
    console.log(`Connected to Kafka message broker`);
}
runProducer().catch(console.error);



const runConsumer = async () => {
    await consumer.connect()
    await consumer.subscribe({ topic: shippingTopic, fromBeginning: true })
    await consumer.run({
        eachMessage: async ({ topic, partition, message, heartbeat, pause }) => {
            // Receive message about shipped items
            console.log(`Received ${message.value}`);
            const shipping = JSON.parse(message.value.toString());
            
            // Get items from shipping
            const items = shipping.items;
            // Each item, broadcast pick up info into MQTT broker
            items.forEach(item => {
                const article = item.article;
                const count = item.count;
                client.publish(`article/${article}/picked`, `${count}`);
                console.log(`Article ${article} ${count}x picked up`);
            });
        }
    })
}
runConsumer().catch(console.error)