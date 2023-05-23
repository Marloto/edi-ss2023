package de.thi.informatik.edi.shop.checkout.controller.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;

public class ShoppingOrderResponse {
	private UUID id;
	private String firstName;
	private String lastName;
	private String zipCode;
	private String city;
	private String street;
	private List<ShoppingOrderItemResponse> items;
	private String status;

	public ShoppingOrderResponse() {
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

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public List<ShoppingOrderItemResponse> getItems() {
		return items;
	}
	
	public String getStatus() {
		return status;
	}
	
	public static ShoppingOrderResponse fromOrder(ShoppingOrder find) {
		ShoppingOrderResponse response = new ShoppingOrderResponse();
		response.id = find.getId();
		response.firstName = find.getFirstName();
		response.lastName = find.getLastName();
		response.street = find.getStreet();
		response.city = find.getCity();
		response.zipCode = find.getZipCode();
		response.items = find.getItems().stream()
				.map(ShoppingOrderItemResponse::fromShoppingOrderItem)
				.collect(Collectors.toList());
		response.status = find.getStatus().toString();
		return response;
	}

}
