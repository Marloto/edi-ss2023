package de.thi.informatik.edi.shop.payment.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.payment.model.Payment;

public class ShoppingOrderResponse {

	private UUID payment;
	private UUID id;

	public ShoppingOrderResponse() {

	}

	public UUID getId() {
		return id;
	}

	public UUID getPayment() {
		return payment;
	}

	public static ShoppingOrderResponse fromPayment(Payment payment) {
		ShoppingOrderResponse response = new ShoppingOrderResponse();
		response.id = payment.getOrderRef();
		response.payment = payment.getId();
		return response;
	}

}
