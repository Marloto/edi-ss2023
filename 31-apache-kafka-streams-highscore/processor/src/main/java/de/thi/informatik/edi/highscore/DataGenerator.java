package de.thi.informatik.edi.highscore;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import de.thi.informatik.edi.highscore.JsonSerdes.JsonSerializer;
import de.thi.informatik.edi.highscore.model.Player;
import de.thi.informatik.edi.highscore.model.Product;
import de.thi.informatik.edi.highscore.model.ScoreEvent;

public class DataGenerator {
	private static Producer<String, Object> producer;

	public static void main(String[] args) throws Exception {
		List<Player> players = Arrays.asList(
				new Player(1L, "SneaX"),
				new Player(2L, "Blackshark"),
				new Player(3L, "Ghost"),
				new Player(4L, "NightWolf")
		);
		List<Product> products = Arrays.asList(
				new Product(1L, "League of Legends"),
				new Product(2L, "Age of Empires II"),
				new Product(3L, "Super Smash Bros"),
				new Product(4L, "Mario Kart")
		);
		
		players.forEach(createRecords(Configurator.PLAYERS, (p) -> p.getId().toString()));
		products.forEach(createRecords(Configurator.PRODUCTS, (p) -> p.getId().toString()));
		
		Consumer<ScoreEvent> records = DataGenerator.<ScoreEvent>createRecords(Configurator.SCORE_EVENTS);
		
		while(true) {
			ScoreEvent event = new ScoreEvent(
					players.get((int)Math.floor(players.size() * Math.random())).getId(),
					products.get((int)Math.floor(products.size() * Math.random())).getId(),
					Math.random() * 1000
			);
			records.accept(event);
			Thread.sleep(1000);
		}
	}
	
	private static <T> Consumer<T> createRecords(String topic)
			throws UnknownHostException, InterruptedException, ExecutionException {
		return createRecords(topic, (p) -> null);
	}

	private static <T> Consumer<T> createRecords(String topic, Function<T, String> key)
			throws UnknownHostException, InterruptedException, ExecutionException {
		Properties config = new Properties();
		Producer<String, Object> producer = getOrCreate(config);
		Runtime.getRuntime().addShutdownHook(new Thread(producer::close));
		return (p) -> {
			String apply = key.apply(p);
			ProducerRecord<String, Object> record = apply != null ? new ProducerRecord<>(topic, apply, p) : new ProducerRecord<>(topic, p);
			Future<RecordMetadata> future = producer.send(record);
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		};
	}

	private static Producer<String, Object> getOrCreate(Properties config) throws UnknownHostException {
		if(producer != null)
			return producer;
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("acks", "all");
		config.put("key.serializer", StringSerializer.class.getName());
		config.put("value.serializer", JsonSerializer.class.getName());
		producer = new KafkaProducer<>(config);
		return producer;
	}
}
