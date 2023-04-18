package de.thi.informatik.edi.shop.payment.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.services.messages.ShoppingOrderMessage;

@Service
public class ShoppingOrderMessageConsumerService {
	
	private static Logger logger = LoggerFactory.getLogger(ShoppingOrderMessageConsumerService.class);
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:payment}")
	private String group;
	@Value("${kafka.orderTopic:order}")
	private String topic;
	
	private KafkaConsumer<String, String> consumer;
	private boolean running;
	private PaymentService payments;

	private TaskExecutor executor;
	
	public ShoppingOrderMessageConsumerService(@Autowired PaymentService payments, @Autowired TaskExecutor executor) {
		this.payments = payments;
		this.executor = executor;
		this.running = true;
	}
	
	@PostConstruct
	private void init() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", servers);
		config.put("group.id", group);
		config.put("key.deserializer", StringDeserializer.class.getName());
		config.put("value.deserializer", StringDeserializer.class.getName());
		logger.info("Connect to " + servers + " as " + config.getProperty("client.id") + "@" + group);
		this.consumer = new KafkaConsumer<>(config);
		logger.info("Subscribe to " + topic);
		this.consumer.subscribe(List.of(topic));
		this.executor.execute(() -> {
			while (running) {
				try {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
					records.forEach(el -> handle(el));
					consumer.commitSync();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}				
		});
	}
	
	private void handle(ConsumerRecord<String, String> el) {
		String value = el.value();
		logger.info("Received message " + value);
		try {
			ShoppingOrderMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, ShoppingOrderMessage.class);
			Payment payment = this.payments.getOrCreateByOrderRef(message.getId());
			logger.info("Update payment " + payment.getId() + " with order " + message.getId());
			this.payments.updateData(payment.getId(), message.getFirstName(), message.getLastName(), message.getStreet(), message.getZipCode(), message.getCity());
			this.payments.updatePrice(payment.getId(), message.getPrice());
			logger.info("Updated " + payment.getId() + " to price " + message.getPrice());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void shutDown() {
		this.running = false;
	}
	
}
