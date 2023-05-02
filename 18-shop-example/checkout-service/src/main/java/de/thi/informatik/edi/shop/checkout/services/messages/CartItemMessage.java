package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

public class CartItemMessage {
	private String name;
	private int count;
	private double price;
	private UUID article;
	
	public UUID getArticle() {
		return article;
	}
	
	

	public String getName() {
		return name;
	}



	public int getCount() {
		return count;
	}



	public double getPrice() {
		return price;
	}



}
