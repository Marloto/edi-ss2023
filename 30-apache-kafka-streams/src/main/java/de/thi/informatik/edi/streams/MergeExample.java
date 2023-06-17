package de.thi.informatik.edi.streams;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;

public class MergeExample {
	public static void main(String[] args) {
		StreamsBuilder builder = new StreamsBuilder();
		


		Map<String, KStream<Void, String>> map = builder.<Void, String>stream("hello-world")
			.split(Named.as("Branch-"))
				.branch((key, value) -> !value.isBlank(), Branched.as("A"))
				.defaultBranch(Branched.as("B"));
		
		map.get("Branch-B")
			.mapValues(value -> "nobody")
			.merge(map.get("Branch-A"))
			.flatMapValues(value -> Arrays.asList(value.split(",")))
			.mapValues(value -> value.trim())
			.mapValues(value -> "Hello, " + value)
			.to("hello-world-answer");
		
		

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev1");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		Topology build = builder.build();
		System.out.println(build.describe());
		KafkaStreams streams = new KafkaStreams(build, config);
		streams.start();

		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
}
