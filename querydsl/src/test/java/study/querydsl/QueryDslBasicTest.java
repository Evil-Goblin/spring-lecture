package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

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
        Member memberB = new Member("memberB", 20, teamA);
        Member memberC = new Member("memberC", 30, teamB);
        Member memberD = new Member("memberD", 40, teamB);

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
    void resultFetch() {
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

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("MemberE", 100));
        em.persist(new Member("MemberF", 100));

        List<Member> fetch = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member memberE = fetch.get(0);
        Member memberF = fetch.get(1);
        Member memberNull = fetch.get(2);

        assertThat(memberE.getUsername()).isEqualTo("MemberE");
        assertThat(memberF.getUsername()).isEqualTo("MemberF");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging1() {
        List<Member> result = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void paging2() {
        QueryResults<Member> queryResults = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    void aggregation() {
        List<Tuple> result = jpaQueryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구하라.
     */
    @Test
    void groupBy() {
        List<Tuple> result = jpaQueryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name) // having 또한 가능하다.
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    void join() {
        List<Member> fetch = jpaQueryFactory
                .selectFrom(member)
                .join(member.team, team) // leftjoin , innerjoin, rightjoin 등 가능
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(fetch)
                .extracting("username")
                .containsExactly("memberA", "memberB");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    void theta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> fetch = jpaQueryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(fetch)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    void joinOnFiltering() {
        List<Tuple> result = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> fetch = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void fetchJoin() {
        em.flush();
        em.clear();

        Member memberA = jpaQueryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("memberA"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(memberA.getTeam());
//        assertThat(loaded).as("페치조인 미적용").isFalse();
        assertThat(loaded).as("페치조인 적용").isTrue();
    }

    @Test
    void subQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(fetch)
                .extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    void selectSubQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> fetch = jpaQueryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    void basicCase() {

        List<String> fetch = jpaQueryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void complexCase() {
        List<String> fetch = jpaQueryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }
}
