package de.thi.informatik.edi.stream.messages;

public class StockChangeMessage {
    private double value;

    public StockChangeMessage() {
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "StockChangeMessage [value=" + value + "]";
    }
}
