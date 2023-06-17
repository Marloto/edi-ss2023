package de.thi.informatik.edi.shop.warehouse.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.warehouse.model.ShippingItem;

public class ShippingItemResponse {
	private UUID article;
	private int count;
	
	public ShippingItemResponse() {
	}
	
	public UUID getArticle() {
		return article;
	}
	
	public int getCount() {
		return count;
	}

	public static ShippingItemResponse fromShippingItem(ShippingItem item) {
		ShippingItemResponse response = new ShippingItemResponse();
		response.article = item.getArticle();
		response.count = item.getCount();
		return response;
	}
}
