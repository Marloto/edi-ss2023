package de.thi.informatik.edi.shop.shopping.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
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
