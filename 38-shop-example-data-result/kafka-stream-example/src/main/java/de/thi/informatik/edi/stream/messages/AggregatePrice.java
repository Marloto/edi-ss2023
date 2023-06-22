package de.thi.informatik.edi.stream.messages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AggregatePrice {
    private double price;
    private int count;
    
    public AggregatePrice() {
        this.price = 0;
        this.count = 0;
    }
    
    public AggregatePrice update(OrderAndPayment data) {
        this.price = data.getOrder().getPrice();
        this.count ++;
        return this;
    }
    
    public int getCount() {
        return count;
    }
    
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "AggregatePrice [price=" + price + ", count=" + count + "]";
    }
}
