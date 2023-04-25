package com.example.example.discount;

import com.example.example.member.Grade;
import com.example.example.member.Member;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10;

    public RateDiscountPolicy() {
        System.out.println("RateDiscountPolicy Constructor");
    }

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        }

        return 0;
    }
}
