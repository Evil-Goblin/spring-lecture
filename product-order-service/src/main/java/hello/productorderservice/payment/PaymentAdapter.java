package hello.productorderservice.payment;

import hello.productorderservice.order.Order;
import hello.productorderservice.product.DiscountPolicy;
import hello.productorderservice.product.Product;

class PaymentAdapter implements PaymentPort {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    PaymentAdapter(PaymentGateway paymentGateway, PaymentRepository paymentRepository) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Order getOrder(Long orderId) {
        return new Order(new Product("상품명", 1000, DiscountPolicy.NONE), 2);
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
