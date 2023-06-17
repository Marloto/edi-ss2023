package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.Cart;

public class CreatedCartMessage extends CartMessage {
	private UUID id;

	public CreatedCartMessage() {
		super("created-cart");
	}

	public UUID getId() {
		return id;
	}

	public static CreatedCartMessage fromCart(Cart cart) {
		CreatedCartMessage message = new CreatedCartMessage();
		message.id = cart.getId();
		return message;
	}
}
