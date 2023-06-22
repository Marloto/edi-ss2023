package de.thi.informatik.edi.stream;

import java.time.Duration;
import java.util.UUID;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.thi.informatik.edi.stream.messages.AggregatePrice;
import de.thi.informatik.edi.stream.messages.OrderAndPayment;
import de.thi.informatik.edi.stream.messages.PaymentMessage;
import de.thi.informatik.edi.stream.messages.ShoppingOrderMessage;

public class ShoppingTopology {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingTopology.class);
    
    public static Topology build() {
            StreamsBuilder builder = new StreamsBuilder();
            
            builder.table("article", Consumed.with(Serdes.UUID(), JsonSerdes.article()))
                .toStream()
                .print(Printed.toSysOut());
            
            builder.stream("cart", Consumed.with(Serdes.UUID(), JsonSerdes.cartMessage()))
                .print(Printed.toSysOut());
            
            
            KTable<UUID, ShoppingOrderMessage> orders = builder.stream("order", Consumed.with(Serdes.UUID(), JsonSerdes.shoppingOrderMessage())).toTable();
//            orders.print(Printed.toSysOut());
            
            KStream<UUID, PaymentMessage> payments = builder.stream("payment", Consumed.with(Serdes.UUID(), JsonSerdes.paymentMessage()));
            payments.print(Printed.toSysOut());
            
            KStream<UUID, PaymentMessage> paymentsByOrderRef = payments
                    .filter((key, value) -> "PAYED".equals(value.getStatus()))
                    .selectKey((key, value) -> value.getOrderRef());
            
            KStream<UUID, OrderAndPayment> joined = paymentsByOrderRef.join(orders, OrderAndPayment::new, Joined.with(Serdes.UUID(), JsonSerdes.paymentMessage(), JsonSerdes.shoppingOrderMessage()));
            joined.print(Printed.toSysOut());
            
            TimeWindows window = TimeWindows.ofSizeAndGrace(Duration.ofSeconds(60), Duration.ofSeconds(5));
            KStream<Windowed<UUID>, AggregatePrice> income = joined
                .groupByKey()
                .windowedBy(window)
                .aggregate(AggregatePrice::new, (key, value, agg) -> agg.update(value), 
                        Materialized.with(Serdes.UUID(), JsonSerdes.aggregatePrice()))
                .toStream();
            income.to("income");
            income.print(Printed.toSysOut());
            
            builder.stream("shipping", Consumed.with(Serdes.UUID(), JsonSerdes.shippingMessage()))
                .print(Printed.toSysOut());
            
            builder.stream("stock-changes", Consumed.with(Serdes.UUID(), Serdes.String()))
                .print(Printed.toSysOut());
            
            return builder.build();
    }
}
