package com.example.example.order;

import com.example.example.discount.DiscountPolicy;
import com.example.example.discount.FixDiscountPolicy;
import com.example.example.member.Member;
import com.example.example.member.MemberRepository;
import com.example.example.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
