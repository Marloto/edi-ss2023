package de.thi.informatik.edi.stream.messages;

import java.util.UUID;

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

    public String toString() {
        return "ArticleMessage [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + "]";
    }

}
