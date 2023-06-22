package de.thi.informatik.edi.shop.warehouse.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.warehouse.model.Shipping;
import de.thi.informatik.edi.shop.warehouse.services.messages.ShippingMessage;

@Service
public class ShippingMessageProducerService {
	
	private static Logger logger = LoggerFactory.getLogger(ShippingMessageProducerService.class);
	
	private MessageProducerService messages;
	
	@Value("${kafka.shippingTopic:shipping}")
	private String topic;

	public ShippingMessageProducerService(@Autowired MessageProducerService messages) {
		this.messages = messages;
	}

	public void shipped(Shipping shipping) {
		logger.info("Send message for updating order " + shipping.getId());
		this.messages.send(topic, shipping.getOrderRef().toString(), ShippingMessage.fromOrder(shipping));
	}

}
