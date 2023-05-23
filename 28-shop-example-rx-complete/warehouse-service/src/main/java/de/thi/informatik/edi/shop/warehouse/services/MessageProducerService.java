package de.thi.informatik.edi.shop.warehouse.services;

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

@Service
public class MessageProducerService {
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:foo}")
	private String group;
	
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
	
	public void send(String topic, String key, Object obj) {
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, asJsonString(obj));
		producer.send(record);
	}

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
