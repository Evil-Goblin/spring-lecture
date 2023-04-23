package com.example.example.discount;

import com.example.example.member.Grade;
import com.example.example.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
            return discountFixAmount;

        return 0;
    }
}
