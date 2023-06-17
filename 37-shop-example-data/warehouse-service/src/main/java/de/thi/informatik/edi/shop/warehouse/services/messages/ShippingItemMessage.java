package de.thi.informatik.edi.shop.warehouse.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.warehouse.model.ShippingItem;

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

	public static ShippingItemMessage fromShippingItem(ShippingItem item) {
		ShippingItemMessage response = new ShippingItemMessage();
		response.article = item.getArticle();
		response.count = item.getCount();
		return response;
	}
}
