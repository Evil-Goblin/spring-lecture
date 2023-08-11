package hello.selector;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

public class ImportSelectorTest {

    @Test
    void staticConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(StaticClass.class);
        HelloBean bean = applicationContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Test
    void selectorConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SelectorConfig.class);
        HelloBean bean = applicationContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Import(HelloConfig.class)
    @Configuration
    public static class StaticClass {

    }

    @Import(HelloImportSelector.class)
    @Configuration
    public static class SelectorConfig {

    }
}
