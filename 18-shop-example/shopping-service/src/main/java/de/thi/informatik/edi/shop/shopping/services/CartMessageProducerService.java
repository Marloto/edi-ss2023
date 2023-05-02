package de.thi.informatik.edi.shop.shopping.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.shopping.model.Cart;
import de.thi.informatik.edi.shop.shopping.services.messages.CartMessage;

@Service
public class CartMessageProducerService {

	private Producer<String, String> producer;

	@PostConstruct
	public void setUp() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("acks", "all");
		config.put("key.serializer", StringSerializer.class.getName());
		config.put("value.serializer", StringSerializer.class.getName());
		producer = new KafkaProducer<>(config);
	}

	public void broadcastCart(Cart cart) {
		// Service startet -> config + producer
		try {
			CartMessage message = CartMessage.fromCart(cart);
			ProducerRecord<String, String> record = new ProducerRecord<>("cart", message.getId().toString(),
					asJsonString(message));
			Future<RecordMetadata> future = producer.send(record);
			RecordMetadata metadata = future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
