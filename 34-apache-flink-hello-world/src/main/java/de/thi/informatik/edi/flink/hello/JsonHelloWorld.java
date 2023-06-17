package de.thi.informatik.edi.flink.hello;

import java.io.IOException;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JsonHelloWorld {

    public static class HelloWorld {
        private String name;

        public HelloWorld() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class HelloWorldSchema extends AbstractDeserializationSchema<HelloWorld> {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        public HelloWorld deserialize(byte[] message) throws IOException {
            return OBJECT_MAPPER.readValue(message, HelloWorld.class);
        }
    }

    public static void main(String[] args) throws Exception {
        final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<HelloWorld> source = KafkaSource.<HelloWorld>builder()
                .setBootstrapServers(bootstrapServers)
                .setTopics("hello-world")
                .setGroupId("flink-foo")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new HelloWorldSchema()).build();

        KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new SimpleStringSchema())
                .setTopic("hello-world-answer")
                .build();

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setRecordSerializer(serializer)
                .build();

        env.fromSource(source, WatermarkStrategy.noWatermarks(), "hello-world")
                .map((value) -> "Hello, " + value)
                .sinkTo(sink);

        env.execute("Hello World");
    }
}
