package hello.productorderservice.payment;

import hello.productorderservice.ApiTest;
import hello.productorderservice.order.OrderSteps;
import hello.productorderservice.payment.application.service.PaymentRequest;
import hello.productorderservice.product.ProductSteps;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static hello.productorderservice.payment.PaymentSteps.주문결제요청_생성;

public class PaymentApiTest extends ApiTest {

    @Test
    void 상품주문() {
        ProductSteps.상품등록요청(ProductSteps.상품등록요청_생성());
        OrderSteps.상품주문요청(OrderSteps.상품주문요청_생성());
        final PaymentRequest request = 주문결제요청_생성();

        ExtractableResponse<Response> response = PaymentSteps.주문결제요청(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
