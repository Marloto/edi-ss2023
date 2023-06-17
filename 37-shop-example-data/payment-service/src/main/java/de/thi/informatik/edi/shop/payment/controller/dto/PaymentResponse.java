package de.thi.informatik.edi.shop.payment.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.payment.model.Payment;

public class PaymentResponse {
	private UUID id;
	private UUID orderRef;
	private String zipCode;
	private String city;
	private String street;
	private String lastName;
	private String firstName;
	private String status;
	private double price;
	
	public PaymentResponse() {
	}

	public UUID getId() {
		return id;
	}

	public UUID getOrderRef() {
		return orderRef;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getStatus() {
		return status;
	}

	public static PaymentResponse fromPayment(Payment payment) {
		PaymentResponse response = new PaymentResponse();
		response.id = payment.getId();
		response.orderRef = payment.getOrderRef();
		response.firstName = payment.getFirstName();
		response.lastName = payment.getLastName();
		response.street = payment.getStreet();
		response.city = payment.getCity();
		response.zipCode = payment.getZipCode();
		response.price = payment.getPrice();
		response.status = payment.getStatus().name();
		return response;
	}
}
