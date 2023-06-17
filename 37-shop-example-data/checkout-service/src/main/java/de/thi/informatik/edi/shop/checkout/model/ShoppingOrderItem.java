package de.thi.informatik.edi.shop.checkout.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ShoppingOrderItem {

	@Id
	private UUID id;

	private UUID article;

	private String name;

	private double price;

	private int count;

	public ShoppingOrderItem() {
		this.id = UUID.randomUUID();
	}

	public ShoppingOrderItem(UUID article, String name, double price, int count) {
		this();
		this.article = article;
		this.name = name;
		this.price = price;
		this.count = count;
	}

	public UUID getId() {
		return id;
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

	public void update(String name, double price, int count) {
		this.name = name;
		this.price = price;
		this.count = count;
	}
}
