package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // Spring Boot 3 이상부터는 @Controller 어노테이션이 있어야 컨트롤러로 인식한다.
public interface OrderControllerV1 {

    @GetMapping("/v1/request")
    String request(@RequestParam(value = "itemId", required = false) String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
