package de.thi.informatik.edi.stream.messages;

public class OrderAndPayment {
    private ShoppingOrderMessage order;
    private PaymentMessage payment;

    public OrderAndPayment(PaymentMessage payment, ShoppingOrderMessage order) {
        this.order = order;
        this.payment = payment;
    }

    public ShoppingOrderMessage getOrder() {
        return order;
    }

    public PaymentMessage getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "OrderAndPayment [order=" + order + ", payment=" + payment + "]";
    }
}
