package de.thi.informatik.edi.shop.checkout.controller.dto;

public class ModifyOrderRequest {
	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;

	public ModifyOrderRequest() {
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

}
