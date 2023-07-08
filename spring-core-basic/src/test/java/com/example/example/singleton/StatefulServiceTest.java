package com.example.example.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

    @Test
    void statefulServiceSingleton() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService bean = applicationContext.getBean(StatefulService.class);
        StatefulService bean2 = applicationContext.getBean(StatefulService.class);

        int priceA = bean.order("userA", 10000);
        int priceB = bean2.order("userB", 20000);

        System.out.println("priceA = " + priceA);
        System.out.println("priceB = " + priceB);
    }
}