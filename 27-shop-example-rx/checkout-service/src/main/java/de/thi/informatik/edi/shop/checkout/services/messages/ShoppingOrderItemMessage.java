package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrderItem;

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

	public static ShoppingOrderItemMessage fromShoppingOrderItem(ShoppingOrderItem item) {
		ShoppingOrderItemMessage response = new ShoppingOrderItemMessage();
		response.article = item.getArticle();
		response.count = item.getCount();
		response.price = item.getPrice();
		response.name = item.getName();
		return response;
	}
}
