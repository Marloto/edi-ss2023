package de.thi.informatik.edi.shop.shopping.model;

public class StockChangedMessage {
	private String article;
	private int stock;

	public StockChangedMessage(String article, int stock) {
		super();
		this.article = article;
		this.stock = stock;
	}

	public String getArticle() {
		return article;
	}

	public int getStock() {
		return stock;
	}
}
