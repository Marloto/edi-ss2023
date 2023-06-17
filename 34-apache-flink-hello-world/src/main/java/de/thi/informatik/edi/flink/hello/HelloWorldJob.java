package de.thi.informatik.edi.flink.hello;

import java.util.Arrays;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class HelloWorldJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromCollection(Arrays.asList("abc", "test", "unknown"))
                .map((value) -> "Hello, " + value)
                .print();

        env.execute("Hello World");
    }
}
