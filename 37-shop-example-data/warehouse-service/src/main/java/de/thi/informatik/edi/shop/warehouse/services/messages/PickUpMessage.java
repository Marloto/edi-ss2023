package de.thi.informatik.edi.shop.warehouse.services.messages;

public class PickUpMessage {
	private double value;
	
	public PickUpMessage() {
	}
	
	public PickUpMessage(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}
