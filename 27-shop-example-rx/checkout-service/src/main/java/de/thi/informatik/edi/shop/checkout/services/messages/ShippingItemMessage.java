package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

public class ShippingItemMessage {
	private UUID article;
	private int count;
	
	public ShippingItemMessage() {
	}
	
	public UUID getArticle() {
		return article;
	}
	
	public int getCount() {
		return count;
	}

}
