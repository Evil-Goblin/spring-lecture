package hello.order.v1.v0;

import hello.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV1 implements OrderService {

    private final MeterRegistry registry;
    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV1(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();

        Counter.builder("my.order") // /actuator/metrics/my.order
                .tag("class", this.getClass().getName()) // /actuator/metrics/my.order?tag=class:hello.order.v1.v0.OrderServiceV1
                .tag("method", "order") // /actuator/metrics/my.order?tag=method:order
                .description("order")
                .register(registry).increment();
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();

        Counter.builder("my.order")
                .tag("class", this.getClass().getName()) // /actuator/metrics/my.order?tag=class:hello.order.v1.v0.OrderServiceV1
                .tag("method", "cancel") // /actuator/metrics/my.order?tag=method:cancel
                .description("order")
                .register(registry).increment();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
