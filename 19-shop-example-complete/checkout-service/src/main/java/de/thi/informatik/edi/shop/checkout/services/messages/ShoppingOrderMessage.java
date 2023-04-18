package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;

public class ShoppingOrderMessage {

	private UUID id;
	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;
	private String status;
	private double price;
	private List<ShoppingOrderItemMessage> items;

	public ShoppingOrderMessage() {
	}

	public UUID getId() {
		return id;
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

	public String getStatus() {
		return status;
	}

	public double getPrice() {
		return price;
	}
	
	public List<ShoppingOrderItemMessage> getItems() {
		return items;
	}

	public static ShoppingOrderMessage fromOrder(ShoppingOrder order) {
		ShoppingOrderMessage message = new ShoppingOrderMessage();
		message.id = order.getId();
		message.firstName = order.getFirstName();
		message.lastName = order.getLastName();
		message.street = order.getStreet();
		message.zipCode = order.getZipCode();
		message.city = order.getCity();
		message.status = order.getStatus().name();
		message.price = order.getPrice();
		message.items = order.getItems().stream()
				.map(ShoppingOrderItemMessage::fromShoppingOrderItem)
				.collect(Collectors.toList());
		return message;
	}

}
