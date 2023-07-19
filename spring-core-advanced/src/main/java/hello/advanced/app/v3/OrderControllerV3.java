package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderServiceV3;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = trace.begin("OrderController.request()");
        try {
            orderServiceV3.orderItem(itemId);
        } catch (Exception e) {
            trace.exception(status, e);
            throw new RuntimeException(e);
        }
        trace.end(status);
        return "ok";
    }
}
