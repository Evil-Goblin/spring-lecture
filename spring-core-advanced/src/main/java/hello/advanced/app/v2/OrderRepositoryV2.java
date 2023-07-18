package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceId traceId, String itemId) {

        TraceStatus status = trace.beginSync(traceId, "OrderRepository.save()");
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
