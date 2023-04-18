package de.thi.informatik.edi.shop.shopping.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CartEntry {
	@Id
    private UUID id;
	private int count;
	private double price;
	private String name;

    @ManyToOne
	private Cart cart;
	
	public CartEntry() {
	}
	
	private CartEntry(UUID id, String name, double price, Cart cart) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.cart = cart;
		this.count = 1;
	}
	
	public Cart getCart() {
		return cart;
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

	@Override
	public int hashCode() {
		return Objects.hash(count, id, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartEntry other = (CartEntry) obj;
		return Objects.equals(id, other.id);
	}

	public static CartEntry fromArticle(Article article, Cart cart) {
		return new CartEntry(article.getId(), article.getName(), article.getPrice(), cart);
	}

	public void increase() {
		this.count ++;
	}

}
