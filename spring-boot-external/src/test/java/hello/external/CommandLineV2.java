package hello.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Set;

@Slf4j
public class CommandLineV2 {

    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }

        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        log.info("SourceArgs = {}", List.of(applicationArguments.getSourceArgs()));
        log.info("NotOptionArgs = {}", applicationArguments.getNonOptionArgs());
        log.info("OptionNames = {}", applicationArguments.getOptionNames());

        Set<String> optionNames = applicationArguments.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option arg {}={}", optionName, applicationArguments.getOptionValues(optionName));
        }

        List<String> a = applicationArguments.getOptionValues("a");
        List<String> b = applicationArguments.getOptionValues("b");
        List<String> c = applicationArguments.getOptionValues("c");
        List<String> d = applicationArguments.getOptionValues("d");

        log.info("a = {}", a);
        log.info("b = {}", b);
        log.info("c = {}", c);
        log.info("d = {}", d);
    }
}
