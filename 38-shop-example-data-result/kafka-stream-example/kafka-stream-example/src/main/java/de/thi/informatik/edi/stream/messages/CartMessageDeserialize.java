package de.thi.informatik.edi.stream.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CartMessageDeserialize extends JsonDeserializer<CartMessage> {

    @Override
    public CartMessage deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(p);
        Class<? extends CartMessage> instanceClass = CartMessage.class;
        if (root.has("type")) {
            String type = root.get("type").textValue();
            if ("added-to-cart".equals(type)) {
                instanceClass = ArticleAddedToCartMessage.class;
            } else if ("deleted-from-cart".equals(type)) {
                instanceClass = DeleteArticleFromCartMessage.class;
            } else if ("created-cart".equals(type)) {
                instanceClass = CreatedCartMessage.class;
            }
            return mapper.convertValue(root, instanceClass);
        }
        return new CartMessage("unknown");
    }

}
