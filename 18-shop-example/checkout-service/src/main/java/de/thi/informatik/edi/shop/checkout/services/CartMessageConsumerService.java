package de.thi.informatik.edi.shop.checkout.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.checkout.services.messages.CartMessage;

@Service
public class CartMessageConsumerService {
	private TaskExecutor executor;
	private boolean running;
	private KafkaConsumer<String, String> consumer;
	private ShoppingOrderService orders;

	public CartMessageConsumerService(@Autowired TaskExecutor executor, @Autowired ShoppingOrderService orders) {
		this.executor = executor;
		this.orders = orders;
		this.running = true;
	}

	@PostConstruct
	private void init() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("group.id", "checkout-service");
		config.put("key.deserializer", StringDeserializer.class.getName());
		config.put("value.deserializer", StringDeserializer.class.getName());

		consumer = new KafkaConsumer<>(config);

		consumer.subscribe(List.of("cart"));

		this.executor.execute(() -> {
			while (running) {
				try {
					// nebeneffekte wenn dienste lange zeit benötigen, um Ereignisse
					// zu verarbeiten?
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
					records.forEach(el -> {
						handleCartMessage(el);
					});
					consumer.commitSync();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void handleCartMessage(ConsumerRecord<String, String> record) {
		String value = record.value();
		String key = record.key();

		try {
			CartMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(value, CartMessage.class);
			message.getItems().forEach(item -> orders.addItemToOrderByCartRef(message.getId(), item.getArticle(),
					item.getName(), item.getPrice(), item.getCount()));
			System.out.println("Items added: " + message.getItems().size());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}
}
