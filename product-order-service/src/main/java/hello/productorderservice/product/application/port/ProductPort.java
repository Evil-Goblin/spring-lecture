package hello.productorderservice.product.application.port;

import hello.productorderservice.product.domain.Product;

public interface ProductPort {

    void save(Product product);

    Product getProduct(Long productId);
}
