package de.thi.informatik.edi.streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;

public class SplitExample01 {
	public static void main(String[] args) {
		StreamsBuilder builder = new StreamsBuilder();

		builder.<Void, String>stream("hello-world")
			.split()
				.branch((key, value) -> !value.isBlank(), 
						Branched.withConsumer(subStream -> subStream
								.to("hello-world-answer")))
				.defaultBranch(
						Branched.withConsumer(ks -> ks
								.to("hello-world-failed")));
		

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev1");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.start();

		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
}
