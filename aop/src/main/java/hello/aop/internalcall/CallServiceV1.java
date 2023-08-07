package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callService;

    // 자기 자신을 주입받을때 setter를 이용
    // springboot 2.6 부터 순환 참조가 금지되어 세터를 통한 주입도 불가능하다.
    // spring.main.allow-circular-references=true 옵션 적용 필요
    @Autowired
    public void setCallService(CallServiceV1 callService) {
        log.info("callServiceV1 setter={}", callService.getClass());
        this.callService = callService;
    }

    // 자기 자신을 주입하려할 때 생성자 주입은 불가능하다. (순환참조)
//    @Autowired
//    public CallServiceV1(CallServiceV1 callService) {
//        this.callService = callService;
//    }

    public void external() {
        log.info("Call external");
        callService.internal();
    }

    public void internal() {
        log.info("Call internal");
    }
}
