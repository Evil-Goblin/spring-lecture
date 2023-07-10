package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }

    @Test
    void internalCall() {
        callService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {

        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {

        public void external() {
            log.info("call external");
            printTxInfo();
            internal(); // @Transactional 자동 호출(실질적으로 대상 객체 내의 메서드가 대상 객체 내의 다른 메서드를 호출)은 런타임 시 실제 트랜잭션을 발생시키지 않습니다
            // @Transactional 의 원리는 실제 함수를 감싸는 프록시를 만들어 그 프록시에서 트랜잭션을 사용하는 것
            // 하지만 이렇게 트랜잭션이 없는 코드에서 호출하게 되면 트랜잭션 프록시가 아닌 external이 아닌 본래의 external이 호출된다.
            // 이후 external에서 internal을 바로 호출하면 this.internal이 호출되게 되어 프록시 인스턴스의 internal이 아닌 실제 인스턴스의 internal이 호출된다.
            // 결국 트랜잭션이 적용되지 않게 된다.
        }

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

}
