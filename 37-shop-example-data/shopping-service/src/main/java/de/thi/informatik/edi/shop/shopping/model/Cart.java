package de.thi.informatik.edi.shop.shopping.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cart {
	@Id
    private UUID id;
	private List<CartEntry> entries;
	private double price;
    
    public Cart() {
    	this.entries = new ArrayList<>();
    	this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}
	
	public List<CartEntry> getEntries() {
		return entries;
	}
	
	public double getPrice() {
		return price;
	}
	
	public CartEntry addArticle(Article article) {
		CartEntry entry = CartEntry.fromArticle(article);
		int index = this.entries.indexOf(entry);
		if(index != -1) {
			entry = this.entries.get(index);
			entry.increase();
		} else {
			this.entries.add(entry);
		}
		this.updatePrice();
		return entry;
	}

	private void updatePrice() {
		this.price = this.entries.stream().map(el -> el.getPrice() * el.getCount()).reduce(0.0, (a, b) -> a + b);
	}

	public CartEntry deleteArticle(UUID article) {
		for(int i = 0; i < this.entries.size(); i ++) {
			if(this.entries.get(i).getId().equals(article)) {				
				return this.entries.remove(i);
			}
		}
		throw new IllegalArgumentException("Unknown article with ID " + article.toString());
	}

}
