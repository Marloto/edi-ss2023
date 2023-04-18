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
import de.thi.informatik.edi.shop.shopping.services.messages.ArticleAddedToCartMessage;
import de.thi.informatik.edi.shop.shopping.services.messages.CreatedCartMessage;
import de.thi.informatik.edi.shop.shopping.services.messages.DeleteArticleFromCartMessage;

@Service
public class CartService {
	
	@Value("${cart.topic:cart}")
	private String topic;
	
	private CartRepository carts;
	private ArticleService articles;
	private MessageProducerService messages;

	public CartService(@Autowired CartRepository carts, @Autowired ArticleService articles, @Autowired MessageProducerService messages) {
		this.carts = carts;
		this.articles = articles;
		this.messages = messages;
	}
	
	public UUID createCart() {
		Cart entity = new Cart();
		this.carts.save(entity);
		this.messages.send(topic, entity.getId().toString(), CreatedCartMessage.fromCart(entity));
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
		this.messages.send(topic, cart.get().getId().toString(), ArticleAddedToCartMessage.fromCartEntry(entry));
		this.carts.save(cart.get());
	}

	public void deleteArticle(UUID id, UUID article) {
		Optional<Cart> cart = this.getCart(id);
		if(cart.isEmpty()) {
			throw new IllegalArgumentException("Element with ID " + id.toString() + " not found");
		}
		CartEntry entry = cart.get().deleteArticle(article);
		this.messages.send(topic, cart.get().getId().toString(), DeleteArticleFromCartMessage.fromCartEntry(entry));
		this.carts.save(cart.get());
	}

	public void cartFinished(UUID id) {
		Optional<Cart> cart = this.getCart(id);
		if(cart.isEmpty()) {
			throw new IllegalArgumentException("Element with ID " + id.toString() + " not found");
		}
	}
}
