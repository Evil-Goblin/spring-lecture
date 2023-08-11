package hello.datasource;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MyDataSource {

    private String url;
    private String username;
    private String password;
    private int maxConnection;
    private Duration timeout;
    private List<String > option;

    public MyDataSource(String url, String username, String password, int maxConnection, Duration timeout, List<String> option) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxConnection = maxConnection;
        this.timeout = timeout;
        this.option = option;
    }

    @PostConstruct
    public void init() {
        log.info("url = {}", url);
        log.info("username = {}", username);
        log.info("password = {}", password);
        log.info("maxConnection = {}", maxConnection);
        log.info("timeout = {}", timeout);
        log.info("option = {}", option);
    }
}
