package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

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
}
