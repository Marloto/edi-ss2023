package de.thi.informatik.edi.shop.shopping.services;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class IotMessageBrokerService {

    private static final Logger logger = LoggerFactory.getLogger(IotMessageBrokerService.class);

    @Value("${mqtt.broker:tcp://localhost:1883}")
    private String broker;

    @Value("${mqtt.client:warehouse-adapter}")
    private String clientId;

    private MqttClient client;

    @PostConstruct
    private void setUp() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            this.client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    private void send(String topic, byte[] message) {
        try {
            client.publish(topic, new MqttMessage(message));
        } catch (MqttException e) {
            e.printStackTrace();
            logger.error("Error while handling: " + new String(message), e);
        }
    }

    public void publish(Flux<Tuple2<String, String>> events) {
        events.subscribe(el -> send(el.getT1(), el.getT2().getBytes()));
    }

    public Flux<Tuple2<String, String>> create(String topic) {
        Many<Tuple2<String, String>> many = Sinks.many().multicast().onBackpressureBuffer();
        Flux<Tuple2<String, String>> asFlux = many.asFlux();
        try {
            client.subscribe(topic, (t, msg) -> {
                many.tryEmitNext(Tuples.of(t, new String(msg.getPayload())));
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return asFlux;
    }
}
