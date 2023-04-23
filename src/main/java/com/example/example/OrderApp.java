package com.example.example;

import com.example.example.member.Grade;
import com.example.example.member.Member;
import com.example.example.member.MemberService;
import com.example.example.member.MemberServiceImpl;
import com.example.example.order.Order;
import com.example.example.order.OrderService;
import com.example.example.order.OrderServiceImpl;

public class OrderApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member  = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order => " + order);
        System.out.println("order.cal => " + order.calculatePrice());
    }
}
