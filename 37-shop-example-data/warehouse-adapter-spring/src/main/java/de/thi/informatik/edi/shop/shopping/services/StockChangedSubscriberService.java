package de.thi.informatik.edi.shop.shopping.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.shopping.model.StockChangedMessage;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@Service
public class StockChangedSubscriberService {
    private IotMessageBrokerService broker;

    private static final Pattern p = Pattern.compile("article/(.*?)/stock");

    private Flux<StockChangedMessage> stockChanges;

    public StockChangedSubscriberService(@Autowired IotMessageBrokerService broker) {
        this.broker = broker;
    }

    @PostConstruct
    private void init() {
        stockChanges = broker.create("article/+/stock")
                .map(el -> Tuples.of(p.matcher(el.getT1()), el.getT2()))
                .filter(el -> el.getT1().find())
                .map(el -> new StockChangedMessage(
                        el.getT1().group(1),
                        Integer.parseInt(el.getT2())));
    }

    public Flux<StockChangedMessage> getStockChanges() {
        return stockChanges;
    }
}
