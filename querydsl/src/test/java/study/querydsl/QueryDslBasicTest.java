package study.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@Transactional
@SpringBootTest
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void init() {
        jpaQueryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 10, teamA);
        Member memberB = new Member("memberB", 10, teamA);
        Member memberC = new Member("memberC", 10, teamB);
        Member memberD = new Member("memberD", 10, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
    }

    @Test
    void startJPQL() {
        // find memberA
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "memberA")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    void startQueryDsl() {
//        QMember m = new QMember("m");
        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .from(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    void search() {
        Member memberA = jpaQueryFactory
                .selectFrom(member)
                .where(member.username.eq("memberA")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(memberA.getUsername()).isEqualTo("memberA");

        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .where(
                        member.username.startsWith("member"), // , 을 이용할 시 and 연산이 된다.
                        (member.age.between(10, 30)))
                .fetch();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }
}
