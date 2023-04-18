package de.thi.informatik.edi.shop.shopping.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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

import de.thi.informatik.edi.shop.shopping.services.messages.StockChangeMessage;

@Service
public class StockChangeMessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(StockChangeMessageConsumerService.class);
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:shopping}")
	private String group;
	@Value("${kafka.stockTopic:stock-changes}")
	private String topic;
	
	private KafkaConsumer<String, String> consumer;
	private boolean running;
	private StockService stocks;

	private TaskExecutor executor;
	
	public StockChangeMessageConsumerService(@Autowired StockService stocks, @Autowired TaskExecutor executor) {
		this.stocks = stocks;
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
			StockChangeMessage message = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(value, StockChangeMessage.class);
			logger.info("Update article " + el.key() + " to stock " + message.getValue());
			UUID article = UUID.fromString(el.key());
			stocks.updateStock(article, message.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void shutDown() {
		this.running = false;
	}
}
