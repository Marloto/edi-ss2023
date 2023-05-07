package de.thi.informatik.edi.shop.warehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Shipping {
	@Id
	private UUID id;
	private List<ShippingItem> items;
	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;
	private UUID orderRef;
	private UUID shippingIdentifier;
	private ShippingStatus status;
	
	public Shipping() {
		this.id = UUID.randomUUID();
		this.items = new ArrayList<>();
		this.status = ShippingStatus.CREATED;
	}
	
	public UUID getOrderRef() {
		return orderRef;
	}

	public UUID getId() {
		return id;
	}
	
	public List<ShippingItem> getItems() {
		return items;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreet() {
		return street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public void update(UUID orderRef, String firstName, String lastName, String street, String zipCode, String city) {
		this.orderRef = orderRef;
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}

	public void addArticle(UUID article, int count) {
		for(ShippingItem item : this.items) {
			if(item.getArticle().equals(article)) {
				item.setCount(count);
				return;
			}
		}
		this.items.add(new ShippingItem(article, count));
	}

	public void doShipping() {
		this.shippingIdentifier = UUID.randomUUID();
		this.status = ShippingStatus.SHIPPED;
	}
	
	public UUID getShippingIdentifier() {
		return shippingIdentifier;
	}
	
	public ShippingStatus getStatus() {
		return status;
	}
}
