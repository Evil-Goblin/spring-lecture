package hello.introduction.service;

import hello.introduction.domain.Member;
import hello.introduction.repository.MemberRepository;
import hello.introduction.repository.MemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MemberServiceTest {

    MemberRepository repository;
    MemberService memberService;

    @BeforeEach
    void beforeEach() {
        repository = new MemoryMemberRepository();
        memberService = new MemberService(repository);
    }

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

    @Test
    void findMembers() {
        Member member1 = new Member();
        member1.setName("test1");

        Member member2 = new Member();
        member2.setName("test2");

        memberService.join(member1);
        memberService.join(member2);

        List<Member> members = memberService.findMembers();

        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void findOne() {
        Member member = new Member();
        member.setName("test");

        Long id = memberService.join(member);

        assertThat(memberService.findOne(id).get()).isEqualTo(member);
    }

}
