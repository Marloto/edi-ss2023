package de.thi.informatik.edi.shop.warehouse.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ShippingItem {

	@Id
	private UUID id;
	private UUID article;
	private int count;
	@ManyToOne
	private Shipping shipping;

	public ShippingItem() {
		this.id = UUID.randomUUID();
	}

	public ShippingItem(Shipping shipping, UUID article, int count) {
		this();
		this.shipping = shipping;
		this.article = article;
		this.count = count;
	}

	public UUID getId() {
		return id;
	}

	public Shipping getShipping() {
		return shipping;
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
