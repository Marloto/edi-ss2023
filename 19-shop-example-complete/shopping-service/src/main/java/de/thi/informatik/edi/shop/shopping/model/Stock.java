package de.thi.informatik.edi.shop.shopping.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {
	@Id
	private UUID article;
	private double stock;

	public Stock() {
	}

	public Stock(UUID article) {
		this.article = article;
	}

	public UUID getArticle() {
		return article;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double value) {
		stock = value;
		
	}
}
