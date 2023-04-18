package de.thi.informatik.edi.shop.payment.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.model.PaymentStatus;

public class PaymentMessage {
	private UUID id;
	private UUID orderRef;
	private String status;
	private String statusBefore;

	public PaymentMessage() {
	}

	public UUID getId() {
		return id;
	}

	public UUID getOrderRef() {
		return orderRef;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusBefore() {
		return statusBefore;
	}

	public static PaymentMessage fromPayment(Payment payment, PaymentStatus before) {
		PaymentMessage message = new PaymentMessage();
		message.id = payment.getId();
		message.orderRef = payment.getOrderRef();
		message.status = payment.getStatus().name();
		message.statusBefore = before.name();
		return message;
	}
}
