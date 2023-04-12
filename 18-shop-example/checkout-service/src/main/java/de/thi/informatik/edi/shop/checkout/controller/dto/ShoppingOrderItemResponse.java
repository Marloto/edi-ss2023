package de.thi.informatik.edi.shop.checkout.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrderItem;

public class ShoppingOrderItemResponse {
	private UUID article;
	private String name;
	private double price;
	private int count;
	
	public ShoppingOrderItemResponse() {
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


	public static ShoppingOrderItemResponse fromShoppingOrderItem(ShoppingOrderItem item) {
		ShoppingOrderItemResponse response = new ShoppingOrderItemResponse();
		response.article = item.getArticle();
		response.count = item.getCount();
		response.price = item.getPrice();
		response.name = item.getName();
		return response;
	}
}
