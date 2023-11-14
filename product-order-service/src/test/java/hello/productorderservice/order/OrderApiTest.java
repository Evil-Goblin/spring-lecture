package hello.productorderservice.order;

import hello.productorderservice.ApiTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static hello.productorderservice.product.ProductSteps.*;

public class OrderApiTest extends ApiTest {

    @Test
    void 상품주문() {
        상품등록요청(상품등록요청_생성());

        ExtractableResponse<Response> response = 상품주문요청(상품주문요청_생성());

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
