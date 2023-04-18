package de.thi.informatik.edi.shop.checkout.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.checkout.services.messages.ShippingMessage;

@Service
public class ShippingMessageConsumerService extends MessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(ShippingMessageConsumerService.class);

	@Value("${kafka.shippingTopic:shipping}")
	private String topic;
	
	private ShoppingOrderService orders;
	
	public ShippingMessageConsumerService(@Autowired ShoppingOrderService orders, @Autowired TaskExecutor executor) {
		super(executor);
		this.orders = orders;
	}
	
	protected void handle(ConsumerRecord<String, String> el) {
		String value = el.value();
		logger.info("Received message " + value);
		try {
			ShippingMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, ShippingMessage.class);
			logger.info("Update order " + message.getOrderRef());
			if("SHIPPED".equals(message.getStatus())) {				
				this.orders.updateOrderIsShipped(message.getOrderRef());
			} else {
				logger.info("Unknown shipping status " + message.getStatus() + " for order " + message.getOrderRef());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected String getClientId() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName() + "-shipping";
	}
	
	protected String getTopic() {
		return topic;
	}
}
