package de.thi.informatik.edi.shop.shopping.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class PickedMessagePublisherService {
    private IotMessageBrokerService broker;

    public PickedMessagePublisherService(@Autowired IotMessageBrokerService broker) {
        this.broker = broker;
    }

    public void handle(Flux<Tuple2<UUID, Integer>> picked) {
        broker.publish(picked
                .map(el -> Tuples.of("article/" + el.getT1().toString() + "/picked", el.getT2().toString())));
    }

}
