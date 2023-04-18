package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.CartEntry;

public class ArticleAddedToCartMessage extends CartMessage {
	private UUID id;
	private UUID article;
	private String name;
	private int count;
	private double price;

	public ArticleAddedToCartMessage() {
		super("added-to-cart");
	}

	public UUID getArticle() {
		return article;
	}

	public UUID getId() {
		return id;
	}
	
	public int getCount() {
		return count;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public static ArticleAddedToCartMessage fromCartEntry(CartEntry entry) {
		ArticleAddedToCartMessage message = new ArticleAddedToCartMessage();
		message.article = entry.getId();
		message.id = entry.getCart().getId();
		message.name = entry.getName();
		message.price = entry.getPrice();
		message.count = entry.getCount();
		return message;
	}
}
