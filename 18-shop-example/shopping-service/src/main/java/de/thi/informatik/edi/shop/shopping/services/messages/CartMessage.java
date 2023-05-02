package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.thi.informatik.edi.shop.shopping.model.Cart;

public class CartMessage {
	private UUID id;
	private List<CartItemMessage> items;
	
	public UUID getId() {
		return id;
	}
	
	public List<CartItemMessage> getItems() {
		return items;
	}
	
	public static CartMessage fromCart(Cart cart) {
		CartMessage message = new CartMessage();
		message.id = cart.getId();
		message.items = cart.getEntries().stream()
				.map(CartItemMessage::fromCartItem)
				.collect(Collectors.toList());
		return message;
	}
}
