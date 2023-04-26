package com.example.example.order;

import com.example.example.annotation.MainDiscountPolicy;
import com.example.example.discount.DiscountPolicy;
import com.example.example.member.Member;
import com.example.example.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@Component("service")
@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, /*@Qualifier("mainDiscountPolicy")*/@MainDiscountPolicy DiscountPolicy discountPolicy) {
        System.out.println("OrderServiceImpl Constructor");
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //TEST
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
