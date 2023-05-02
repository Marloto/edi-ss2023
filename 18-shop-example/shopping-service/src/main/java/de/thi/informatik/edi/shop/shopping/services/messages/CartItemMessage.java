package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.CartEntry;

public class CartItemMessage {
	private String name;
	private int count;
	private double price;
	private UUID article;
	
	public UUID getArticle() {
		return article;
	}
	
	

	public String getName() {
		return name;
	}



	public int getCount() {
		return count;
	}



	public double getPrice() {
		return price;
	}



	public static CartItemMessage fromCartItem(CartEntry item) {
		CartItemMessage message = new CartItemMessage();
		message.article = item.getId();
		message.price = item.getPrice();
		message.count = item.getCount();
		message.name = item.getName();
		return message;
	}
}
