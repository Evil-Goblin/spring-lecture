package hello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RestController
public class TrafficController {

    @GetMapping("cpu")
    public String cpu() {
        log.info("cpu");
        long value = 0;
        for (long i = 0; i < 10000000000L; i++) {
            value++;
        }
        return "ok value=" + value;
    }

    @GetMapping("jvm")
    public String jvm() {
        Long value = 0L;
        log.info("jvm");
        for (Long i = 0L; i < 10000000000L; i++) {
            value++;
        }

        return "ok value=" + value;
    }

    @Autowired
    DataSource dataSource;

    @GetMapping("jdbc")
    public String jdbc() throws SQLException {
        log.info("jdbc");
        Connection connection = dataSource.getConnection();
        log.info("connection info={}", connection);
        // conn.close(); // 커넥션을 닫지 않는다.
        return "ok";
    }

    @GetMapping("error-log")
    public String errorLog() {
        log.error("error log");
        return "error";
    }
}
