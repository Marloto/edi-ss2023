const { Kafka, Partitioners } = require('kafkajs')

const kafka = new Kafka({
  clientId: 'example-producer',
  brokers: ['localhost:9092']
})

const producer = kafka.producer({ createPartitioner: Partitioners.DefaultPartitioner })

const run = async () => {
  // Producing
  await producer.connect()

  let i = 0;
  setInterval(async () => {
    await producer.send({
      topic: 'test-topic',
      messages: [
        { value: JSON.stringify({time: new Date().getTime(), rnd: Math.random()}) },
      ],
    })
  }, 1000);
}

run().catch(console.error)