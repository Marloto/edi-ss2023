package de.thi.informatik.edi.shop.shopping.services.messages;

import java.util.UUID;

import de.thi.informatik.edi.shop.shopping.model.Article;

public class ArticleMessage {
    private UUID id;
    private String name;
    private String description;
    private double price;

    public ArticleMessage() {
    }

    public String getDescription() {
        return description;
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

    public static ArticleMessage fromArticle(Article article) {
        ArticleMessage m = new ArticleMessage();
        m.id = article.getId();
        m.name = article.getName();
        m.description = article.getDescription();
        m.price = article.getPrice();
        return m;
    }
}
