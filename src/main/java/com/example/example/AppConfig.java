package com.example.example;

import com.example.example.discount.DiscountPolicy;
import com.example.example.discount.FixDiscountPolicy;
import com.example.example.discount.RateDiscountPolicy;
import com.example.example.member.MemberRepository;
import com.example.example.member.MemberService;
import com.example.example.member.MemberServiceImpl;
import com.example.example.member.MemoryMemberRepository;
import com.example.example.order.OrderService;
import com.example.example.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
