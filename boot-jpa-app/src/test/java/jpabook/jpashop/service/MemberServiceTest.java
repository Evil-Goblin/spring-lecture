package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService service;

    @Autowired
    MemberRepository repository;

    @Autowired
    EntityManager em;

    @Test
    void 회원가입() {
        Member member = new Member();
        member.setName("kim");

        Long savedId = service.join(member);

        em.flush();
        assertThat(member).isEqualTo(repository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() {
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        service.join(member1);

        assertThatThrownBy(() -> service.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }
}
