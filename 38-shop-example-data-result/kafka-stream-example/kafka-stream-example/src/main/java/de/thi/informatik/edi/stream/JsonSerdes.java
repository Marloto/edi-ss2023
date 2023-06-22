package de.thi.informatik.edi.stream;

import java.io.IOException;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.stream.messages.AggregatePrice;
import de.thi.informatik.edi.stream.messages.ArticleMessage;
import de.thi.informatik.edi.stream.messages.CartMessage;
import de.thi.informatik.edi.stream.messages.PaymentMessage;
import de.thi.informatik.edi.stream.messages.PickUpMessage;
import de.thi.informatik.edi.stream.messages.ShippingMessage;
import de.thi.informatik.edi.stream.messages.ShoppingOrderMessage;
import de.thi.informatik.edi.stream.messages.StockChangeMessage;

public class JsonSerdes {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerdes.class);

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Serde<CartMessage> cartMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, CartMessage.class));
    }
    
    public static Serde<PaymentMessage> paymentMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, PaymentMessage.class));
    }
    
    public static Serde<PickUpMessage> pickUpMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, PickUpMessage.class));
    }
    
    public static Serde<ShippingMessage> shippingMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, ShippingMessage.class));
    }
    
    public static Serde<ShoppingOrderMessage> shoppingOrderMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, ShoppingOrderMessage.class));
    }
    
    public static Serde<StockChangeMessage> stockChangeMessage() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, StockChangeMessage.class));
    }
    
    public static Serde<ArticleMessage> article() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, ArticleMessage.class));
    }

    private static <T> T readValue(byte[] data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            logger.error("Error while reading " + new String(data), e);
            return null;
        }
    }

    private static <T> byte[] writeValue(T data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (IOException e) {
            logger.error("Error while writing " + data, e);
            return new byte[0];
        }
    }

    public static class JsonSerializer implements Serializer<Object> {
        public byte[] serialize(String topic, Object data) {
            return JsonSerdes.writeValue(data);
        }
    }

    public static Serde<AggregatePrice> aggregatePrice() {
        return Serdes.serdeFrom(
                (topic, data) -> writeValue(data),
                (topic, data) -> readValue(data, AggregatePrice.class));
    }
}
