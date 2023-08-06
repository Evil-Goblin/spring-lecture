package hello.aop.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class OrderRepository {

    public String save(String itemId) {
        log.info("[orderRepository] execute");

        // save logic

        if ("ex".equals(itemId)) {
            throw new IllegalStateException("예외 발생!");
        }
        return "ok";
    }
}
