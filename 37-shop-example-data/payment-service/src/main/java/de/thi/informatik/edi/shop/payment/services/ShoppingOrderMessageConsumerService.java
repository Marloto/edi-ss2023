package de.thi.informatik.edi.shop.payment.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.services.messages.ShoppingOrderMessage;

@Service
public class ShoppingOrderMessageConsumerService extends MessageConsumerService {
	
	private static Logger logger = LoggerFactory.getLogger(ShoppingOrderMessageConsumerService.class);
	
	@Value("${kafka.orderTopic:order}")
	private String topic;
	
	private PaymentService payments;

	public ShoppingOrderMessageConsumerService(@Autowired PaymentService payments) {
		this.payments = payments;
	}
	
	protected void handle(ConsumerRecord<String, String> el) {
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
			logger.error("Error while handling message", e);
		}
	}
	
	protected String getClientId() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName() + "-shopping-order";
	}
	
	protected String getTopic() {
		return topic;
	}
	
}
