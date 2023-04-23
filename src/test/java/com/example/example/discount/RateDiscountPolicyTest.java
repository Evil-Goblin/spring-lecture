package com.example.example.discount;

import com.example.example.member.Grade;
import com.example.example.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% Discount")
    void vip_o() {
        //given
        Member memverVIP = new Member(1L, "memverVIP", Grade.VIP);
        //when
        int discount = discountPolicy.discount(memverVIP, 10000);
        //then
        assertEquals(discount, 1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인NO")
    void vip_x() {
        //given
        Member memverBasic = new Member(1L, "memverBasic", Grade.BASIC);
        //when
        int discount = discountPolicy.discount(memverBasic, 10000);
        //then
        assertEquals(discount, 0);
    }
}