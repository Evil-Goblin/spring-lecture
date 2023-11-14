package hello.productorderservice.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServicePOJOTest {

    private ProductService productService;

    private StupProductPort productPort = new StupProductPort();

    @BeforeEach
    void setUp() {
        productService = new ProductService(productPort);
    }

    @Test
    void 상품수정() {

        final Long productId = 1L;
        final UpdateProductRequest request = new UpdateProductRequest("상품 수정", 2000, DiscountPolicy.NONE);
        Product product = new Product("상품명", 1000, DiscountPolicy.NONE);
        productPort.getProduct_will_return = product;

        productService.updateProduct(productId, request);

        assertThat(product.getName()).isEqualTo("상품 수정");
        assertThat(product.getPrice()).isEqualTo(2000);
    }

    private static class StupProductPort implements ProductPort {
        public Product getProduct_will_return;

        @Override
        public void save(Product product) {

        }

        @Override
        public Product getProduct(Long productId) {
            return getProduct_will_return;
        }
    }
}
