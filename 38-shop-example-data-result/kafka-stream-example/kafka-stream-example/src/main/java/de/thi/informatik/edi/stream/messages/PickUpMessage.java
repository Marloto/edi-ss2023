package de.thi.informatik.edi.stream.messages;

public class PickUpMessage {
    private double value;

    public PickUpMessage() {
    }

    public PickUpMessage(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "PickUpMessage [value=" + value + "]";
    }
}
