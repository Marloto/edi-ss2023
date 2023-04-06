package de.thi.informatik.edi.kafka;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleAsyncProducer {
	private static Logger logger = LoggerFactory.getLogger(ExampleProducer.class);
	public static void main(String[] args) throws Exception {
		final String topic = "test-topic";
		
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("acks", "all");
		config.put("key.serializer", StringSerializer.class.getName());
		config.put("value.serializer", StringSerializer.class.getName());
		try(Producer<String, String> producer = new KafkaProducer<>(config)) {			
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, "test", "abc");
			producer.send(record, 
					(metadata, e) -> logger.info(metadata.toString()));
		}
	}

}
