package de.thi.informatik.edi.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingTopology {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingTopology.class);
    
    public static Topology build() {
            StreamsBuilder builder = new StreamsBuilder();
            
            builder.table("article", Consumed.with(Serdes.UUID(), JsonSerdes.article()))
                .toStream()
                .print(Printed.toSysOut());
            
            builder.stream("cart", Consumed.with(Serdes.UUID(), JsonSerdes.cartMessage()))
                .print(Printed.toSysOut());
            
            builder.stream("order", Consumed.with(Serdes.UUID(), JsonSerdes.shoppingOrderMessage()))
                .print(Printed.toSysOut());
            
            builder.stream("payment", Consumed.with(Serdes.UUID(), JsonSerdes.paymentMessage()))
                .print(Printed.toSysOut());
            
            builder.stream("shipping", Consumed.with(Serdes.UUID(), JsonSerdes.shippingMessage()))
                .print(Printed.toSysOut());
            
            builder.stream("stock-changes", Consumed.with(Serdes.UUID(), Serdes.String()))
                .print(Printed.toSysOut());
            
            return builder.build();
    }
}
