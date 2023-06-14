package hello.introduction.service;

import hello.introduction.domain.Member;
import hello.introduction.repository.MemberRepository;
import hello.introduction.repository.MemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired
    MemberRepository repository;

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("test");

        // when
        Long join = memberService.join(member);

        // then
        Member findMember = memberService.findOne(join).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("test");

        Member member2 = new Member();
        member2.setName("test");

        // when
        memberService.join(member1);

        // then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });

        assertThat(illegalStateException.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}
