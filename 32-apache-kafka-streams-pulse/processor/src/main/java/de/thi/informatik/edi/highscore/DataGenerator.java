package de.thi.informatik.edi.highscore;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
import de.thi.informatik.edi.highscore.model.BodyTemp;
import de.thi.informatik.edi.highscore.model.Pulse;

public class DataGenerator {
	private static Producer<String, Object> producer;
	private static boolean running = true;

	public static void main(String[] args) throws Exception {
		Consumer<Pulse> pulse = DataGenerator.<Pulse>createRecords(Configurator.PULSE_EVENTS, (e) -> "1");
		Consumer<BodyTemp> body = DataGenerator.<BodyTemp>createRecords(Configurator.BODY_TEMP_EVENTS, (e) -> "1");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> running = false));
		
		new Thread(() -> {
			while(running) {
				pulse.accept(new Pulse(simpleDateFormat.format(new Date())));
				try {
					Thread.sleep((int)Math.floor(Math.random() * 150 + 550));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(() -> {
			while(running) {
				body.accept(new BodyTemp(simpleDateFormat.format(new Date()), Math.random() * 3 + 36.5, "C"));
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
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
