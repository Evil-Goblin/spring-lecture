package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId) {

        TraceStatus status = trace.begin("OrderRepository.save()");
        try {
            if ("ex".equals(itemId)) {
                throw new IllegalArgumentException("예외 발생!");
            }
            sleep(1000);
        } catch (Exception e) {
            trace.exception(status, e);
            throw new RuntimeException(e);
        }
        trace.end(status);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
