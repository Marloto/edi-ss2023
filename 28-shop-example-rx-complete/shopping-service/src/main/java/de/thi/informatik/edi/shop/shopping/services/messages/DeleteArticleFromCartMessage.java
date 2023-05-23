package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.Cart;
import de.thi.informatik.edi.shop.shopping.model.CartEntry;

public class DeleteArticleFromCartMessage extends CartMessage {
	private UUID id;
	private UUID article;
	
	public DeleteArticleFromCartMessage() {
		super("deleted-from-cart");
	}
	
	public UUID getArticle() {
		return article;
	}
	
	public UUID getId() {
		return id;
	}
	
	public static DeleteArticleFromCartMessage fromCartEntry(CartEntry entry, Cart cart) {
		DeleteArticleFromCartMessage message = new DeleteArticleFromCartMessage();
		message.article = entry.getId();
		message.id = cart.getId();
		return message;
	}
}
