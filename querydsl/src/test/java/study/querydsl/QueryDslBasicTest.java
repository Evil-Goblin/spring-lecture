package study.querydsl;

import com.querydsl.core.QueryResults;
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

    @Test
    public void resultFetch() {
        List<Member> fetch = jpaQueryFactory
                .selectFrom(member)
                .fetch(); // list 조회

        Member fetchOne = jpaQueryFactory
                .selectFrom(member)
                .fetchOne(); // 단건 조회. 결과 없을시 null 리턴. 결과 여럿인 경우 NonUniqueResultException

        Member fetchFirst = jpaQueryFactory
                .selectFrom(member)
                .fetchFirst(); // limit(1).fetchOne() 과 동일한 코드

        QueryResults<Member> fetchResults = jpaQueryFactory
                .selectFrom(member)
                .fetchResults(); // getTotal 제공, getResults 를 통해 결과 리스트를 조회할 수 있다.
        // fetchResults 는 deprecate 되었다.
        // 이유는 select 기반으로 count 쿼리를 작성하게 되는 여러개의 그룹, having 절 등 복잡한 쿼리에 대해서 문제가 들어남에 의해 deprecate
        // Blaze-Persistence, BlazeJPAQuery 와 같은 고급 쿼리 기능이 있지만 왠만큼 카운트가 엄격히 필요하지 않다면 그냥 fetch를 사용하라.

        long total = fetchResults.getTotal();
        List<Member> results = fetchResults.getResults();

        long fetchCount = jpaQueryFactory
                .selectFrom(member)
                .fetchCount(); // 아마도 같은 이유로 deprecate 되지 않았을까 싶다. 결국 fetchResults 의 문제는 count 쿼리였으니까...
    }
}
