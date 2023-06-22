package de.thi.informatik.edi.shop.shopping.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.shopping.services.messages.ShippingItemMessage;
import de.thi.informatik.edi.shop.shopping.services.messages.ShippingMessage;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;

@Service
public class ShippingMessageConsumerService extends MessageConsumerService {

    private Flux<ShippingItemMessage> shippedItems;

    @PostConstruct
    private void init() {
        shippedItems = this.getMessages()
                .map(el -> el.getT2()) // get message-string
                .map(this::fromMessage) // string to ShippingMessage
                .filter(el -> el.getOrderRef() != null) // check for empty messages
                .flatMap(el -> Flux.fromIterable(el.getItems())); // unpack all items
    }

    public Flux<ShippingItemMessage> getShippedItems() {
        return shippedItems;
    }

    private ShippingMessage fromMessage(String value) {
        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(value, ShippingMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ShippingMessage(); // create empty message-instance, null is not the best solution
    }

    @Override
    protected String getClientId() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName() + "-shipping";
    }

    @Override
    protected String getTopic() {
        return "shipping";
    }
}
