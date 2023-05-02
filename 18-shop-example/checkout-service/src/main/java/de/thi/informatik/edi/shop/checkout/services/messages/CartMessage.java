package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.List;
import java.util.UUID;

public class CartMessage {
	private UUID id;
	private List<CartItemMessage> items;
	
	public UUID getId() {
		return id;
	}
	
	public List<CartItemMessage> getItems() {
		return items;
	}
}
