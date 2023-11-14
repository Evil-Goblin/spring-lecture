package hello.productorderservice.order;

import hello.productorderservice.ApiTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static hello.productorderservice.product.ProductSteps.*;

public class OrderApiTest extends ApiTest {

    @Test
    void 상품주문() {
        상품등록요청(상품등록요청_생성());

        final CreateOrderRequest request = 상품주문요청_생성();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .log().all().extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
