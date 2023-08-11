package hello.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class PayConfig {

    @Bean
    @Profile("default")
    public PayClient localPayClient() {
        log.info("로컬 페이 클라이언트 빈 등록");
        return new LocalPayClient();
    }

    @Bean
    @Profile("prod")
    public PayClient prodPayClient() {
        log.info("운영 페이 클라이언트 빈 등록");
        return new ProdPayClient();
    }
}
