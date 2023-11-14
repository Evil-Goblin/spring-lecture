package hello.productorderservice.payment;

import hello.productorderservice.order.Order;

interface PaymentPort {
    Order getOrder(Long orderId);

    void pay(int price, String cardNumber);

    void save(Payment payment);
}
