package hello.productorderservice.order;

import hello.productorderservice.product.DiscountPolicy;
import hello.productorderservice.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void getTotalPrice() {
        Order order = new Order(new Product("상품명", 1000, DiscountPolicy.NONE), 2);

        int totalPrice = order.getTotalPrice();

        Assertions.assertThat(totalPrice).isEqualTo(2000);
    }

    @Test
    void getTotalPrice_Fix_Discounted() {
        Order order = new Order(new Product("상품명", 2000, DiscountPolicy.FIX_1000_AMOUNT), 2);

        int totalPrice = order.getTotalPrice();

        Assertions.assertThat(totalPrice).isEqualTo(2000);
    }

}
