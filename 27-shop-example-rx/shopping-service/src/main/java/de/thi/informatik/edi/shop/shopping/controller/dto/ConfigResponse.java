package de.thi.informatik.edi.shop.shopping.controller.dto;

public class ConfigResponse {

	private String checkoutUrl;
	private String title;

	public ConfigResponse(String checkoutUrl, String title) {
		this.checkoutUrl = checkoutUrl;
		this.title = title;
	}

	public String getCheckoutUrl() {
		return checkoutUrl;
	}

	public String getTitle() {
		return title;
	}

}
