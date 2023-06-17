package de.thi.informatik.edi.flink.hello;

import java.util.stream.Stream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class DedpulicatorJob {
    public static void main(String[] args) throws Exception {
        final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setTopics("hello-world")
                .setGroupId("flink-foo")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema()).build();

        KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new SimpleStringSchema())
                .setTopic("hello-world-answer")
                .build();

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setRecordSerializer(serializer)
                .build();
        
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "hello-world")
                .<String>flatMap((value, out) -> Stream.of(value.split(",")).forEach(out::collect))
                .returns(Types.STRING)
                .map((value) -> value.trim())
                .keyBy(value -> value)
                .flatMap(new Deduplicator())
                .map((value) -> "Hello, " + value)
                .sinkTo(sink);

        env.execute("Hello World");
    }
    
    public static class Deduplicator extends RichFlatMapFunction<String, String> {
        ValueState<Boolean> keyHasBeenSeen;

        @Override
        public void open(Configuration conf) {
            ValueStateDescriptor<Boolean> desc = new ValueStateDescriptor<>("keyHasBeenSeen", Types.BOOLEAN);
            keyHasBeenSeen = getRuntimeContext().getState(desc);
        }

        @Override
        public void flatMap(String event, Collector<String> out) throws Exception {
            if (keyHasBeenSeen.value() == null) {
                out.collect(event);
                keyHasBeenSeen.update(true);
            }
        }
    }
}
