package de.thi.informatik.edi.shop.shopping.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple3;

@Service
public class MessageProducerService {

    @Value("${kafka.servers:localhost:9092}")
    private String servers;

    private Producer<String, String> producer;

    public MessageProducerService() {
    }

    @PostConstruct
    private void init() throws UnknownHostException {
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", servers);
        config.put("acks", "all");
        config.put("key.serializer", StringSerializer.class.getName());
        config.put("value.serializer", StringSerializer.class.getName());
        producer = new KafkaProducer<>(config);
    }

    public void send(Flux<Tuple3<String, String, Object>> messages) {
        messages.subscribe(el -> {
            ProducerRecord<String, String> record = new ProducerRecord<>(el.getT1(), el.getT2(),
                    asJsonString(el.getT3()));
            producer.send(record);
        });
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
