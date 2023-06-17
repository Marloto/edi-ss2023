package de.thi.informatik.edi.shop.warehouse.controller.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.thi.informatik.edi.shop.warehouse.model.Shipping;

public class ShippingResponse {
	private UUID id;
	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;
	private List<ShippingItemResponse> items;
	private String status;

	public ShippingResponse() {
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
	
	public List<ShippingItemResponse> getItems() {
		return items;
	}
	
	public String getStatus() {
		return status;
	}

	public static ShippingResponse fromShipping(Shipping shipping) {
		ShippingResponse response = new ShippingResponse();
		response.id = shipping.getId();
		response.firstName = shipping.getFirstName();
		response.lastName = shipping.getLastName();
		response.street = shipping.getStreet();
		response.city = shipping.getCity();
		response.zipCode = shipping.getZipCode();
		response.status = shipping.getStatus().name();
		response.items = shipping.getItems().stream()
				.map(ShippingItemResponse::fromShippingItem)
				.collect(Collectors.toList());
		return response;
	}

}
