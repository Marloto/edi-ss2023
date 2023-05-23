package de.thi.informatik.edi.shop.checkout.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.checkout.services.messages.PaymentMessage;
import reactor.core.publisher.Flux;

@Service
public class PaymentMessageConsumerService extends MessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(PaymentMessageConsumerService.class);
	
	@Value("${kafka.paymentTopic:payment}")
	private String topic;
	
	public PaymentMessageConsumerService(@Autowired TaskExecutor executor) {
		super(executor);
	}

	private PaymentMessage deserialize(String value) {
		try {
			return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, PaymentMessage.class);
		} catch (JsonProcessingException e) {
			logger.error("Error while handling message: " + value, e);
			return new PaymentMessage();
		}
	}
	
	public Flux<PaymentMessage> getPaymentMessages() {
		return this.getMessages()
				.map(el -> el.getT2())
				.map(this::deserialize)
				.filter(el -> el.getOrderRef() != null);
	}
	
	protected String getClientId() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName() + "-payment";
	}
	
	protected String getTopic() {
		return topic;
	}
}
