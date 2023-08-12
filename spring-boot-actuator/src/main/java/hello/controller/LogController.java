package hello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {

    /**
     * logging level 변경
     * POST http://localhost:8080/actuator/loggers/hello.controller
     * Content-Type: application/json
     *
     * {
     *   "configuredLevel":"TRACE"
     * }
     */
    @GetMapping("/log")
    public String log() {
        log.trace("trace log");
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
        return "ok";
    }
}
