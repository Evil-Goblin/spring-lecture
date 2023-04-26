package com.example.example.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component("service") // Annotation-specified bean name 'service' for bean class [com.example.example.order.OrderServiceImpl] conflicts with existing, non-compatible bean definition of same name and class [com.example.example.member.MemberServiceImpl]
@Component
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberServiceImpl(MemberRepository memberRepository) {
//        System.out.println("MemberServiceImpl Constructor");
//        this.memberRepository = memberRepository;
//    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // TEST
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
