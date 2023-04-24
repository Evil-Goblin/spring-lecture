package com.example.example.xml;

import com.example.example.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

public class XmlAppContext {

    @Test
    void xmlAppContext() {
        GenericXmlApplicationContext genericXmlApplicationContext = new GenericXmlApplicationContext("appConfig.xml");
        MemberService memberService = genericXmlApplicationContext.getBean("memberService", MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
