package com.example.example;

import com.example.example.member.MemberRepository;
import com.example.example.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.example.example",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

    // Overriding bean definition for bean 'memoryMemberRepository'
    // 수동 등록 Bean이 우선순위를 가진다.
    // boot로 실행시 에러 발생
    // spring.main.allow-bean-definition-overriding=true 설정으로 오버라이딩 허용
//    @Bean(name = "memoryMemberRepository")
//    MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }

}
