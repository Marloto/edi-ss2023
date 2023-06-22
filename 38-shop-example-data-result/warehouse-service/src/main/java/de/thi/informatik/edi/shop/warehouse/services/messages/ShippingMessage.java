package de.thi.informatik.edi.shop.warehouse.services.messages;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.thi.informatik.edi.shop.warehouse.model.Shipping;

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

	public static ShippingMessage fromOrder(Shipping shipping) {
		ShippingMessage message = new ShippingMessage();
		message.orderRef = shipping.getOrderRef();
		message.shippingIdentifier = shipping.getShippingIdentifier();
		message.status = shipping.getStatus().name();
		message.items = shipping.getItems().stream().map(ShippingItemMessage::fromShippingItem)
				.collect(Collectors.toList());
		return message;
	}

}
