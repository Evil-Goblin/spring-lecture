package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentCheck {

    private final Environment environment;

    public EnvironmentCheck(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        String a = environment.getProperty("a");
        String b = environment.getProperty("b");
        String c = environment.getProperty("c");
        String d = environment.getProperty("d");

        log.info("env a={}", a);
        log.info("env b={}", b);
        log.info("env c={}", c);
        log.info("env d={}", d);

        String url = environment.getProperty("url");
        String username = environment.getProperty("username");
        String password = environment.getProperty("password");

        log.info("url = {}", url);
        log.info("username = {}", username);
        log.info("password = {}", password);
    }
}
