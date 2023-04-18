package de.thi.informatik.edi.shop.checkout.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ShoppingOrderItem {

	@Id
	private UUID id;

	private UUID article;

	private String name;

	private double price;

	private int count;

	@ManyToOne
	private ShoppingOrder order;

	public ShoppingOrderItem() {
		this.id = UUID.randomUUID();
	}

	public ShoppingOrderItem(ShoppingOrder order, UUID article, String name, double price, int count) {
		this();
		this.order = order;
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

	public ShoppingOrder getOrder() {
		return order;
	}

	public void update(String name, double price, int count) {
		this.name = name;
		this.price = price;
		this.count = count;
	}
}
