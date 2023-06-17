package de.thi.informatik.edi.flink.hello;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class WindowJob {
    private static int idCounter;
    private static long start = System.currentTimeMillis();
    
    public static class Event {
        public long timestamp = System.currentTimeMillis() - (long) Math.floor(Math.random() * 40000);
        public int id = idCounter ++;
        public String toString() {
            return "Event [timestamp=" + (timestamp - start) + ", id=" + id + "]";
        }
    }
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        WatermarkStrategy<Event> strategy = WatermarkStrategy
                .<Event>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner((event, timestamp) -> event.timestamp);
        
        List<Event> asList = new ArrayList<>();
        for(int i = 0; i < 10; i ++) {
            asList.add(new Event());
        }
        env.fromCollection(asList).print();
        
        env.fromCollection(asList)
                .assignTimestampsAndWatermarks(strategy)
                .keyBy(value -> 1)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .aggregate(new CountAggregator())
                .print();

        env.execute("Hello World");
    }
    
    public static class CountAggregator implements AggregateFunction<Event, Tuple2<List<Event>, Integer>, Tuple2<List<Event>, Integer>> {
        @Override
        public Tuple2<List<Event>, Integer> createAccumulator() {
            return new Tuple2<>(new ArrayList<>(), 0);
        }

        @Override
        public Tuple2<List<Event>, Integer> add(Event event, Tuple2<List<Event>, Integer> accumulator) {
            System.out.println("add " + event.toString());
            accumulator.f0.add(event);
            return new Tuple2<>(accumulator.f0, accumulator.f1 + 1);
        }

        @Override
        public Tuple2<List<Event>, Integer> getResult(Tuple2<List<Event>, Integer> accumulator) {
            return accumulator;
        }

        @Override
        public Tuple2<List<Event>, Integer> merge(Tuple2<List<Event>, Integer> a, Tuple2<List<Event>, Integer> b) {
            ArrayList<Event> f0 = new ArrayList<>(a.f0);
            f0.addAll(b.f0);
            return new Tuple2<>(f0,a.f1 + b.f1);
        }
    }
}
