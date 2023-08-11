package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class CommandLineBean {

    private final ApplicationArguments applicationArguments;


    public CommandLineBean(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @PostConstruct
    public void init() {
        log.info("SourceArgs = {}", List.of(applicationArguments.getSourceArgs()));
        log.info("NotOptionArgs = {}", applicationArguments.getNonOptionArgs());
        log.info("OptionNames = {}", applicationArguments.getOptionNames());

        Set<String> optionNames = applicationArguments.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option arg {}={}", optionName, applicationArguments.getOptionValues(optionName));
        }
    }
}
