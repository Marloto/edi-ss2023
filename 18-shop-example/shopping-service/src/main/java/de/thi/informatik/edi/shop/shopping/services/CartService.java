package de.thi.informatik.edi.shop.shopping.services;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.shopping.model.Article;
import de.thi.informatik.edi.shop.shopping.model.Cart;
import de.thi.informatik.edi.shop.shopping.model.CartEntry;
import de.thi.informatik.edi.shop.shopping.repositories.CartRepository;
import de.thi.informatik.edi.shop.shopping.services.messages.CartMessage;

@Service
public class CartService {
	
	@Value("${cart.topic:cart}")
	private String topic;
	
	private CartRepository carts;
	private ArticleService articles;
	private CartMessageProducerService cartMessages;

	public CartService(@Autowired CartRepository carts, @Autowired ArticleService articles, @Autowired CartMessageProducerService cartMessages) {
		this.carts = carts;
		this.articles = articles;
		this.cartMessages = cartMessages;
	}
	
	public UUID createCart() {
		Cart entity = new Cart();
		this.carts.save(entity);
		return entity.getId();
	}

	public Optional<Cart> getCart(UUID id) {
		return this.carts.findById(id);
	}

	public void addArticle(UUID id, UUID article) {
		Optional<Cart> cart = this.getCart(id);
		if(cart.isEmpty()) {
			throw new IllegalArgumentException("Element with ID " + id.toString() + " not found");
		}
		Optional<Article> articleRef = this.articles.getArticle(article);
		if(articleRef.isEmpty()) {
			throw new IllegalArgumentException("Article with ID " + id.toString() + " not found");
		}
		CartEntry entry = cart.get().addArticle(articleRef.get());
		this.carts.save(cart.get());
	}

	public void deleteArticle(UUID id, UUID article) {
		Optional<Cart> cart = this.getCart(id);
		if(cart.isEmpty()) {
			throw new IllegalArgumentException("Element with ID " + id.toString() + " not found");
		}
		CartEntry entry = cart.get().deleteArticle(article);
		this.carts.save(cart.get());
	}

	public void cartFinished(UUID id) {
		Optional<Cart> cart = this.getCart(id);
		if(cart.isEmpty()) {
			throw new IllegalArgumentException("Element with ID " + id.toString() + " not found");
		}
		
		// Checkout Dienst benötigt Artikel-Nummern, Namen, Preise und Mengen 
		// Kanal für die Information: z.B. cart
		// Nachrichten-Klassen
		
		cartMessages.broadcastCart(cart.get());
	}
}
