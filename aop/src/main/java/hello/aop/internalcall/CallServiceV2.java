package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

//    private final ApplicationContext applicationContext;
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("Call external");
//        CallServiceV2 callService = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callService = callServiceProvider.getObject();
        callService.internal();
    }

    public void internal() {
        log.info("Call internal");
    }
}
