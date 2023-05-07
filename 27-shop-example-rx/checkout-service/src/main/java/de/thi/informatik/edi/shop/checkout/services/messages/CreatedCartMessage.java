package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CreatedCartMessage.class)
public class CreatedCartMessage extends CartMessage {
	private UUID id;

	public CreatedCartMessage() {
		super("cart-created");
	}

	public UUID getId() {
		return id;
	}
}
