package de.thi.informatik.edi.shop.checkout.services.messages;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = DeleteArticleFromCartMessage.class)
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
}
