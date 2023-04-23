package com.example.example;

import com.example.example.member.Grade;
import com.example.example.member.Member;
import com.example.example.member.MemberService;
import com.example.example.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member member1 = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + member1.getName());
    }
}
