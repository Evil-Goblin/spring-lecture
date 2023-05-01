package com.example.example.web;

import com.example.example.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;
    public void logic(String id) {
//        MyLogger myLogger1 = myLoggerProvider.getObject();
        myLogger.log("service id => " + id);
    }
}
