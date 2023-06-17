package de.thi.informatik.edi.shop.shopping.controller.dto;

import java.util.ArrayList;
import java.util.List;

import de.thi.informatik.edi.shop.shopping.model.Cart;

public class CartResponse {
	private List<CartEntryResponse> articles;
	private double price;
	
	public CartResponse() {
		this.articles = new ArrayList<>();
	}
	
	public List<CartEntryResponse> getArticles() {
		return articles;
	}
	
	public double getPrice() {
		return price;
	}

	public static CartResponse fromCart(Cart cart) {
		CartResponse response = new CartResponse();
		cart.getEntries().forEach(el -> response.articles.add(CartEntryResponse.fromCartEntry(el)));
		response.price = cart.getPrice();
		return response;
	}
}
