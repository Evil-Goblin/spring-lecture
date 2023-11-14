package hello.productorderservice.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hello.productorderservice.payment.PaymentSteps.주문결제요청_생성;

public class PaymentServiceTest {

    private PaymentService paymentService;
    private PaymentPort paymentPort;

    @BeforeEach
    void setUp() {
        PaymentGateway paymentGateway = new ConsolePaymentGateway();
        PaymentRepository paymentRepository = new PaymentRepository();
        paymentPort = new PaymentAdapter(paymentGateway, paymentRepository);
        paymentService = new PaymentService(paymentPort);
    }

    @Test
    void 상품주문() {
        final PaymentRequest request = 주문결제요청_생성();
        paymentService.payment(request);
    }
}
