package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.List;
import java.util.UUID;

public class ShippingMessage {

	private String status;
	private UUID shippingIdentifier;
	private UUID orderRef;
	private List<ShippingItemMessage> items;

	public ShippingMessage() {
	}

	public String getStatus() {
		return status;
	}

	public UUID getShippingIdentifier() {
		return shippingIdentifier;
	}

	public UUID getOrderRef() {
		return orderRef;
	}

	public List<ShippingItemMessage> getItems() {
		return items;
	}

}
