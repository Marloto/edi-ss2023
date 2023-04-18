package de.thi.informatik.edi.shop.checkout.services.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CartMessageDeserialize.class)
public class CartMessage {
	private String type;

	public CartMessage() {
	}

	public CartMessage(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
