package hello.productorderservice.order;

import hello.productorderservice.product.Product;

interface OrderPort {

    Product getProductById(Long productId);

    void save(Order order);
}
