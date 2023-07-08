package com.example.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("OK");
    }

    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        responseWriter.write("OK");
    }

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        String body = httpEntity.getBody();

        log.info("body={}", body);

        return new HttpEntity<>("OK");
    }

    @PostMapping("/request-body-string-v3-1")
    public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
        String body = httpEntity.getBody();
        HttpMethod method = httpEntity.getMethod();
        Type type = httpEntity.getType();
        URI url = httpEntity.getUrl();
        HttpHeaders headers = httpEntity.getHeaders();

        log.info("body={}", body);
        log.info("method={}", method);
        log.info("type={}", type);
        log.info("url={}", url);
        log.info("headers={}", headers);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String  requestBodyStringV4(@RequestBody String messageBody) {

        log.info("messageBody={}", messageBody);

        return "OK";
    }
}
