package de.thi.informatik.edi.stream.messages;

import java.util.UUID;

public class ShippingItemMessage {
    private UUID article;
    private int count;

    public ShippingItemMessage() {
    }

    public UUID getArticle() {
        return article;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return "ShippingItemMessage [article=" + article + ", count=" + count + "]";
    }
}
