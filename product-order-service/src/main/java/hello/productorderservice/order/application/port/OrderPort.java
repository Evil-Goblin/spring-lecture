package hello.productorderservice.order.application.port;

import hello.productorderservice.order.domain.Order;
import hello.productorderservice.product.domain.Product;

public interface OrderPort {

    Product getProductById(Long productId);

    void save(Order order);
}
