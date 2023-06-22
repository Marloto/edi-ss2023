package de.thi.informatik.edi.shop.shopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.shopping.model.StockChangedMessage;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@Service
public class StockChangedMessageProducerService {
    private MessageProducerService messages;

    @Value("${kafka.stockTopic:stock-changes}")
    private String topic;

    public StockChangedMessageProducerService(@Autowired MessageProducerService messages) {
        this.messages = messages;
    }

    public void handle(Flux<StockChangedMessage> messages) {
        this.messages.send(messages.map(el -> Tuples.of(topic, el.getArticle(), el.getStock())));
    }
}
