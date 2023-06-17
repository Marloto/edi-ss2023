package de.thi.informatik.edi.stream.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CartMessageDeserialize.class)
public class CartMessage {
    private String type;

    public CartMessage() {
    }

    public CartMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return "CartMessage [type=" + type + "]";
    }

}
