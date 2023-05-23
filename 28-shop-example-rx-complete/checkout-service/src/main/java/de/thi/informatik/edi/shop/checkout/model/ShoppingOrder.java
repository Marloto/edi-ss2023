package de.thi.informatik.edi.shop.checkout.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ShoppingOrder {
	@Id
	private UUID id;
	private UUID cartRef;

	private List<ShoppingOrderItem> items;

	private String firstName;
	private String lastName;
	private String street;
	private String zipCode;
	private String city;
	private ShoppingOrderStatus status;

	public ShoppingOrder() {
		this.id = UUID.randomUUID();
		this.items = new ArrayList<>();
		this.setStatus(ShoppingOrderStatus.CREATED);
	}

	public ShoppingOrder(UUID cartRef) {
		this();
		this.cartRef = cartRef;
	}

	public UUID getCartRef() {
		return cartRef;
	}

	public UUID getId() {
		return id;
	}

	public List<ShoppingOrderItem> getItems() {
		return items;
	}

	public ShoppingOrderItem addItem(UUID article, String name, double price, int count) {
		for (ShoppingOrderItem item : this.items) {
			if (item.getArticle().equals(article)) {
				item.update(name, price, count);
				return item;
			}
		}
		ShoppingOrderItem item = new ShoppingOrderItem(article, name, price, count);
		this.items.add(item);
		return item;
	}

	public void removeItem(UUID article) {
		for (int i = 0; i < this.items.size(); i++) {
			if (items.get(i).getArticle().equals(article)) {
				this.items.remove(i);
				return;
			}
		}
	}

	public void update(String firstName, String lastName, String street, String zipCode, String city) {
		if (this.status == ShoppingOrderStatus.CREATED) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.street = street;
			this.zipCode = zipCode;
			this.city = city;
		} else {
			throw new IllegalStateException("Order has to be in status CREATED but was in " + this.status);
		}
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

	public ShoppingOrderStatus getStatus() {
		return status;
	}

	public void setStatus(ShoppingOrderStatus status) {
		this.status = status;
	}

	public double getPrice() {
		return getItems().stream().map(el -> el.getCount() * el.getPrice()).reduce(0.0, (a, b) -> a + b);
	}

}
