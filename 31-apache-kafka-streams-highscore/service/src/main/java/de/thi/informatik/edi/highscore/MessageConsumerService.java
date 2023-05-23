package de.thi.informatik.edi.highscore;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public abstract class MessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);
	
	@Value("${kafka.servers:localhost:9092}")
	private String servers;
	@Value("${kafka.group:highscore}")
	private String group;
	
	private KafkaConsumer<String, String> consumer;
	private boolean running;
	
	@Autowired
	private TaskExecutor executor;

	private Flux<Tuple2<String, String>> messages;

	private Many<Tuple2<String, String>> many;
	
	public MessageConsumerService() {
		this.running = true;
		this.many = Sinks.many().multicast().onBackpressureBuffer();
		this.messages = many.asFlux();
	}
	
	@PostConstruct
	private void init() throws UnknownHostException {
		Properties config = new Properties();
		config.put("client.id", getClientId());
		config.put("bootstrap.servers", servers);
		config.put("group.id", group);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put("key.deserializer", StringDeserializer.class.getName());
		config.put("value.deserializer", StringDeserializer.class.getName());
		logger.info("Connect to " + servers + " as " + config.getProperty("client.id") + "@" + group);
		this.consumer = new KafkaConsumer<>(config);
		logger.info("Subscribe to " + getTopic());
		
		this.consumer.subscribe(List.of(getTopic()));
		this.executor.execute(() -> {
			while (running) {
				try {					
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
					records.forEach(el -> many.tryEmitNext(Tuples.of(el.key(), el.value())));
					consumer.commitSync();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected abstract String getClientId() throws UnknownHostException;
	
	protected abstract String getTopic();
	
	protected Flux<Tuple2<String, String>> getMessages() {
		return messages;
	}

	@PreDestroy
	private void shutDown() {
		this.running = false;
	}
}
