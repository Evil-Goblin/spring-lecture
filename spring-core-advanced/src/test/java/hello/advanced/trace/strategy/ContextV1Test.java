package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        // logic start
        log.info("biz logic1 start");
        // logic end
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // logic start
        log.info("biz logic2 start");
        // logic end
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    @Test
    void strategyV1() {
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 contextV1 = new ContextV1(strategyLogic1);
        contextV1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 contextV2 = new ContextV1(strategyLogic2);
        contextV2.execute();
    }

    @Test
    void strategyV2() {
        Strategy bizLogic1Anonymous = new Strategy() {
            @Override
            public void call() {
                log.info("biz logic1 anonymous");
            }
        };

        ContextV1 contextV1 = new ContextV1(bizLogic1Anonymous);
        contextV1.execute();

        Strategy bizLogic2Anonymous = new Strategy() {
            @Override
            public void call() {
                log.info("biz logic2 anonymous");
            }
        };

        ContextV1 contextV2 = new ContextV1(bizLogic2Anonymous);
        contextV2.execute();
    }

    @Test
    void strategyV3() {

        ContextV1 contextV1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("biz logic1 anonymous");
            }
        });
        contextV1.execute();

        ContextV1 contextV2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("biz logic2 anonymous");
            }
        });
        contextV2.execute();
    }

    @Test
    void strategyV4() {

        ContextV1 contextV1 = new ContextV1(() -> log.info("biz logic1 anonymous"));
        contextV1.execute();

        ContextV1 contextV2 = new ContextV1(() -> log.info("biz logic2 anonymous"));
        contextV2.execute();
    }
}
