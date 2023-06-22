package de.thi.informatik.edi.shop.shopping.model;

import java.util.Objects;
import java.util.UUID;

public class CartEntry {
    private UUID id;
	private int count;
	private double price;
	private String name;
	
	public CartEntry() {
	}
	
	private CartEntry(UUID id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.count = 1;
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

	public static CartEntry fromArticle(Article article) {
		return new CartEntry(article.getId(), article.getName(), article.getPrice());
	}

	public void increase() {
		this.count ++;
	}

}
