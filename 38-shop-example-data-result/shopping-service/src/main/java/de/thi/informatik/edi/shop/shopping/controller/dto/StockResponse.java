package de.thi.informatik.edi.shop.shopping.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.Stock;

public class StockResponse {

	private UUID article;
	private double stock;

	public StockResponse() {
	}

	public UUID getArticle() {
		return article;
	}

	public double getStock() {
		return stock;
	}
	
	public static StockResponse fromStock(Stock stock) {
		StockResponse resp = new StockResponse();
		resp.article = stock.getArticle();
		resp.stock = stock.getStock();
		return resp;
	}
}
