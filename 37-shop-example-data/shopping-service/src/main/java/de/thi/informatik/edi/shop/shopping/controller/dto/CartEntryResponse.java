package de.thi.informatik.edi.shop.shopping.controller.dto;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.CartEntry;

public class CartEntryResponse {

    private UUID id;
	private int count;
	private double price;
	private String name;
	
	public CartEntryResponse() {
	}
	
	public int getCount() {
		return count;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public static CartEntryResponse fromCartEntry(CartEntry entry) {
		CartEntryResponse response = new CartEntryResponse();
		response.count = entry.getCount();
		response.id = entry.getId();
		response.name = entry.getName();
		response.price = entry.getPrice();
		return response;
	}
}
