package hello.productorderservice.order;

import hello.productorderservice.product.Product;

class OrderService {

    private final OrderPort orderPort;

    OrderService(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    public void createOrder(CreateOrderRequest request) {
        Product product = orderPort.getProductById(request.getProductId());

        Order order = new Order(product, request.quantity());

        orderPort.save(order);
    }
}
