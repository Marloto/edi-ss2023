package de.thi.informatik.edi.stream;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

public class Application {
    private static final String APP_ID = "shopping01";

    public static void main(String[] args) {
            Properties config = new Properties();
            config.put(StreamsConfig.APPLICATION_ID_CONFIG, APP_ID);
            config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass());
            config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

            Topology build = ShoppingTopology.build();
            System.out.println(build.describe());
//            KafkaStreams streams = new KafkaStreams(build, config);
//            streams.start();
//
//            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
