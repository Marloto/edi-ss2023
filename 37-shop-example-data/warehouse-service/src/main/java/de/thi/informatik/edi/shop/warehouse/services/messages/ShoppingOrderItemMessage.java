package de.thi.informatik.edi.shop.warehouse.services.messages;

import java.util.UUID;

public class ShoppingOrderItemMessage {

	private UUID article;
	private String name;
	private double price;
	private int count;

	public ShoppingOrderItemMessage() {
	}

	public UUID getArticle() {
		return article;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getCount() {
		return count;
	}

}
