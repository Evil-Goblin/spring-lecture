package hello.productorderservice.product;

import hello.productorderservice.product.domain.DiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DiscountPolicyTest {

    @Test
    void applyDiscount() {
        int price = 1000;
        int discountedPrice = DiscountPolicy.NONE.applyDiscount(price);

        Assertions.assertThat(discountedPrice).isEqualTo(price);
    }

    @Test
    void applyFixDiscount() {
        int price = 2000;
        int discountedPrice = DiscountPolicy.FIX_1000_AMOUNT.applyDiscount(price);

        Assertions.assertThat(discountedPrice).isEqualTo(1000);
    }

}
