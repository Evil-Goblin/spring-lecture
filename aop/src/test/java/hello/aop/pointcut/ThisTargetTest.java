package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * application.properties
 * spring.aop.proxy-target-class=true   CGLIB
 * spring.aop.proxy-target-class=false  JDK 동적 프록시
 */
@Import(ThisTargetTest.ThisTargetAspect.class)
@Slf4j
@SpringBootTest(properties = "spring.aop.proxy-target-class=false") // JDK 동적 프록시
//@SpringBootTest(properties = "spring.aop.proxy-target-class=true") // CGLIB
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");

        // CGLIB
        // memberService Proxy=class hello.aop.member.MemberServiceImpl$$SpringCGLIB$$0
        // [target-Concrete] String hello.aop.member.MemberServiceImpl.hello(String)
        // [target-interface] String hello.aop.member.MemberServiceImpl.hello(String)
        // [this-Concrete] String hello.aop.member.MemberServiceImpl.hello(String)
        // [this-interface] String hello.aop.member.MemberServiceImpl.hello(String)

        // JDK 동적 프록시
        // memberService Proxy=class jdk.proxy2.$Proxy47
        // [target-Concrete] String hello.aop.member.MemberService.hello(String)
        // [target-interface] String hello.aop.member.MemberService.hello(String)
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThisConcrete(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-Concrete] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTargetConcrete(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-Concrete] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
