package hello.productorderservice.payment.adapter;

import hello.productorderservice.order.domain.Order;
import hello.productorderservice.order.adapter.OrderRepository;
import hello.productorderservice.payment.domain.Payment;
import hello.productorderservice.payment.application.port.PaymentPort;
import org.springframework.stereotype.Component;

@Component
class PaymentAdapter implements PaymentPort {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    PaymentAdapter(PaymentGateway paymentGateway, PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

    @Override
    public void pay(int price, String cardNumber) {
        paymentGateway.execute(price, cardNumber);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
