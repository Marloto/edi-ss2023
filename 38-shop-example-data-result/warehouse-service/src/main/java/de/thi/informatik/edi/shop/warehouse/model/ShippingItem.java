package de.thi.informatik.edi.shop.warehouse.model;

import java.util.UUID;

public class ShippingItem {

	private UUID id;
	private UUID article;
	private int count;

	public ShippingItem() {
		this.id = UUID.randomUUID();
	}

	public ShippingItem(UUID article, int count) {
		this();
		this.article = article;
		this.count = count;
	}

	public UUID getId() {
		return id;
	}

	public UUID getArticle() {
		return article;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
