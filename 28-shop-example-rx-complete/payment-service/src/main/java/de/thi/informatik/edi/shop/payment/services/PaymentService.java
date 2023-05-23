package de.thi.informatik.edi.shop.payment.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.model.PaymentStatus;
import de.thi.informatik.edi.shop.payment.repositories.PaymentRepository;

@Service
public class PaymentService {

	private PaymentRepository payments;
	private PaymentMessageProducerService messages;

	public PaymentService(@Autowired PaymentRepository payments, @Autowired PaymentMessageProducerService messages) {
		this.payments = payments;
		this.messages = messages;
	}

	public Payment findById(UUID id) {
		Optional<Payment> optional = this.payments.findById(id);
		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Unknown payment with ID " + id.toString());
		}
		return optional.get();
	}

	public Payment getOrCreateByOrderRef(UUID orderRef) {
		Optional<Payment> optional = this.payments.findByOrderRef(orderRef);
		Payment payment;
		if (optional.isEmpty()) {
			payment = new Payment(orderRef);
		} else {
			payment = optional.get();
		}
		this.payments.save(payment);
		return payment;
	}

	public void pay(UUID id) {
		Payment payment = this.findById(id);
		PaymentStatus before = payment.getStatus();
		payment.pay();
		this.payments.save(payment);
		this.messages.statusChanged(payment, before);
	}

	public void updatePrice(UUID id, double price) {
		Payment payment = this.findById(id);
		PaymentStatus before = payment.getStatus();
		payment.setPrice(price);
		this.payments.save(payment);
		this.messages.statusChanged(payment, before);
	}

	public void updateData(UUID id, String firstName, String lastName, String street, String zipCode, String city) {
		Payment payment = this.findById(id);
		payment.update(firstName, lastName, street, zipCode, city);
		this.payments.save(payment);
	}

}
