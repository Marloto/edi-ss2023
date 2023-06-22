package de.thi.informatik.edi.shop.shopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactor.util.function.Tuples;

@Service
public class PickedIntegrationService {
    private PickedMessagePublisherService picked;
    private ShippingMessageConsumerService shipped;

    public PickedIntegrationService(@Autowired PickedMessagePublisherService picked,
            @Autowired ShippingMessageConsumerService shipped) {
        this.picked = picked;
        this.shipped = shipped;
    }

    @PostConstruct
    private void init() {
        this.picked.handle(this.shipped.getShippedItems()
                .map(el -> Tuples.of(el.getArticle(), el.getCount())));
    }
}
