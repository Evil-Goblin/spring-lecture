package com.example.example.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Configuration
    static class LifeCycleConfig {

        // destroyMethod 의 default 값인 (inferred) 는 설정하지 않아도 호출된다.
        // initMethod 는 default 값이 없다.
        @Bean/*(initMethod = "init", destroyMethod = "close")*/
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://test.dev");
            return networkClient;
        }
    }

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext configurableApplicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = configurableApplicationContext.getBean(NetworkClient.class);
        configurableApplicationContext.close();
    }
}
