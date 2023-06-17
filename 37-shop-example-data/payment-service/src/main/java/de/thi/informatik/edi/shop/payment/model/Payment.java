package de.thi.informatik.edi.shop.payment.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payment {
	@Id
	private UUID id;
	private UUID orderRef;
	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;
	private double price;
	private PaymentStatus status;
	private LocalDateTime createdTime;
	private LocalDateTime payedTime;

	public Payment() {
		this.id = UUID.randomUUID();
		this.status = PaymentStatus.CREATED;
		this.createdTime = LocalDateTime.now();
	}

	public Payment(UUID orderRef) {
		this();
		this.orderRef = orderRef;
	}

	public UUID getId() {
		return id;
	}

	public UUID getOrderRef() {
		return orderRef;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreet() {
		return street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public double getPrice() {
		return price;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void pay() {
		this.status = PaymentStatus.PAYED;
		this.payedTime = LocalDateTime.now();
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public LocalDateTime getPayedTime() {
		return payedTime;
	}

	public void setPrice(double price) {
		this.price = price;
		this.status = PaymentStatus.PAYABLE;
	}

	public void update(String firstName, String lastName, String street, String zipCode, String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}

}
