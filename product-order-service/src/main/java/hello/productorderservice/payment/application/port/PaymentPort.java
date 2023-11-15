package hello.productorderservice.payment.application.port;

import hello.productorderservice.order.domain.Order;
import hello.productorderservice.payment.domain.Payment;

public interface PaymentPort {
    Order getOrder(Long orderId);

    void pay(int price, String cardNumber);

    void save(Payment payment);
}
