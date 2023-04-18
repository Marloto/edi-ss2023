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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.checkout.services.messages.ArticleAddedToCartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.CartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.CreatedCartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.DeleteArticleFromCartMessage;

@Service
public class CartMessageConsumerService {
	
	private static Logger logger = LoggerFactory.getLogger(CartMessageConsumerService.class);
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:checkout}")
	private String group;
	@Value("${kafka.cartTopic:cart}")
	private String topic;
	
	private KafkaConsumer<String, String> consumer;
	private boolean running;
	private ShoppingOrderService orders;

	private TaskExecutor executor;
	
	public CartMessageConsumerService(@Autowired ShoppingOrderService orders, @Autowired TaskExecutor executor) {
		this.orders = orders;
		this.executor = executor;
		this.running = true;
	}
	
	@PostConstruct
	private void init() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName() + "-cart");
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
			CartMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, CartMessage.class);
			if(message instanceof ArticleAddedToCartMessage) {
				logger.info("Article added to cart " + ((ArticleAddedToCartMessage) message).getId());
				this.orders.addItemToOrderByCartRef(
						((ArticleAddedToCartMessage) message).getId(), 
						((ArticleAddedToCartMessage) message).getArticle(), 
						((ArticleAddedToCartMessage) message).getName(), 
						((ArticleAddedToCartMessage) message).getPrice(), 
						((ArticleAddedToCartMessage) message).getCount());
			} else if(message instanceof DeleteArticleFromCartMessage) {
				logger.info("Article removed from cart " + ((DeleteArticleFromCartMessage) message).getId());
				this.orders.deleteItemFromOrderByCartRef(
						((DeleteArticleFromCartMessage) message).getId(), 
						((DeleteArticleFromCartMessage) message).getArticle());
			} else if(message instanceof CreatedCartMessage) {
				logger.info("Cart created " + ((CreatedCartMessage) message).getId());
				this.orders.createOrderWithCartRef(((CreatedCartMessage) message).getId());
				
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void shutDown() {
		this.running = false;
	}
	
}
