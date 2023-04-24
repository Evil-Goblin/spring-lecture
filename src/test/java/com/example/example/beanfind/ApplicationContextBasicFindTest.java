package com.example.example.beanfind;

import com.example.example.AppConfig;
import com.example.example.member.MemberService;
import com.example.example.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// 이거 둘이 종류가 달랐네......
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("Bean 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        assertInstanceOf(MemberServiceImpl.class, memberService);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = applicationContext.getBean(MemberService.class);
        assertInstanceOf(MemberServiceImpl.class, memberService);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberService memberService = applicationContext.getBean("memberService", MemberServiceImpl.class);
//        assertInstanceOf(MemberServiceImpl.class, memberService);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("Bean 이름으로 조회 실패")
    void findBeanNameFailed() {
        assertThrows(NoSuchBeanDefinitionException.class, () ->
                applicationContext.getBean("xxxxx", MemberService.class));
    }
}
