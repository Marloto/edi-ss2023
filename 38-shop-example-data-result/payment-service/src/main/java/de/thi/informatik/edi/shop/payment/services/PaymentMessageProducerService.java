package de.thi.informatik.edi.shop.payment.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.model.PaymentStatus;
import de.thi.informatik.edi.shop.payment.services.messages.PaymentMessage;

@Service
public class PaymentMessageProducerService {
	
	private static Logger logger = LoggerFactory.getLogger(PaymentMessageProducerService.class);
	
	private MessageProducerService messages;
	
	@Value("${kafka.paymentTopic:payment}")
	private String topic;

	public PaymentMessageProducerService(@Autowired MessageProducerService messages) {
		this.messages = messages;
	}

	public void statusChanged(Payment payment, PaymentStatus before) {
		logger.info("Send message for payed order " + payment.getOrderRef() + " in payment " + payment.getId());
		this.messages.send(topic, payment.getOrderRef().toString(), PaymentMessage.fromPayment(payment, before));
	}

}
