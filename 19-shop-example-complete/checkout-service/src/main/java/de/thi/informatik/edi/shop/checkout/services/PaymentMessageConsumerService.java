package de.thi.informatik.edi.shop.checkout.services;

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

import de.thi.informatik.edi.shop.checkout.services.messages.PaymentMessage;

@Service
public class PaymentMessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(PaymentMessageConsumerService.class);
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:checkout}")
	private String group;
	@Value("${kafka.paymentTopic:payment}")
	private String topic;
	
	private KafkaConsumer<String, String> consumer;
	private boolean running;
	private ShoppingOrderService orders;

	private TaskExecutor executor;
	
	public PaymentMessageConsumerService(@Autowired ShoppingOrderService orders, @Autowired TaskExecutor executor) {
		this.orders = orders;
		this.executor = executor;
		this.running = true;
	}
	
	@PostConstruct
	private void init() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName() + "-payment");
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
			PaymentMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, PaymentMessage.class);
			logger.info("Update order " + message.getOrderRef());
			if("PAYED".equals(message.getStatus())) {				
				this.orders.updateOrderIsPayed(message.getOrderRef());
			} else if("PAYABLE".equals(message.getStatus())) {
				logger.info("Ignore status change " + message.getStatus() + " for order " + message.getOrderRef() + " and payment " + message.getId());
			} else {
				logger.info("Unknown status change " + message.getStatus() + " for order " + message.getOrderRef() + " and payment " + message.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void shutDown() {
		this.running = false;
	}
}
