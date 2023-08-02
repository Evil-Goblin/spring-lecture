package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        A beanA = annotationConfigApplicationContext.getBean("BeanA", A.class);
        beanA.helloA();

        Assertions.assertThatThrownBy(() -> annotationConfigApplicationContext.getBean(B.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);

    }

    @Configuration
    static class BasicConfig {
        @Bean(name = "BeanA")
        public A a() {
            return new A();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }
}
