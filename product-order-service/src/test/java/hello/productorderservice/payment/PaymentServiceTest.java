package hello.productorderservice.payment;

import hello.productorderservice.order.OrderService;
import hello.productorderservice.order.OrderSteps;
import hello.productorderservice.product.ProductService;
import hello.productorderservice.product.ProductSteps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hello.productorderservice.payment.PaymentSteps.주문결제요청_생성;

@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Test
    void 상품주문() {
        productService.addProduct(ProductSteps.상품등록요청_생성());
        orderService.createOrder(OrderSteps.상품주문요청_생성());
        final PaymentRequest request = 주문결제요청_생성();

        paymentService.payment(request);
    }
}
