package de.thi.informatik.edi.shop.shopping.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.Article;

public class ArticleResponse {
	private UUID id;
	private String name;
	private String description;
	private String image;
	private double price;
	private int quantity;

	public ArticleResponse() {
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public static ArticleResponse fromArticle(Article article) {
		ArticleResponse a = new ArticleResponse();
		a.id = article.getId();
		a.name = article.getName();
		a.description = article.getDescription();
		a.price = article.getPrice();
		a.quantity = article.getQuantity();
		a.image = article.getImage();
		return a;
	}
}
