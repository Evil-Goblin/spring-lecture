package proxyrelation.lazyload;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class LazyLoadMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");

            entityManager.persist(team);

            Member member = new Member();
            member.setName("memberA");
            member.setTeam(team);

            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            /**
             * select
             *     member0_.id as id1_0_0_,
             *     member0_.name as name2_0_0_,
             *     member0_.TEAM_ID as team_id3_0_0_
             * from
             *     Member member0_
             * where
             *     member0_.id=?
             */
            Member findMember = entityManager.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            System.out.println("findTeam().getClass() = " + findTeam.getClass()); // class proxyrelation.lazyload.Team$HibernateProxy$lT2V3iJF (프록시)

            /**
             * select
             *     team0_.id as id1_1_0_,
             *     team0_.name as name2_1_0_
             * from
             *     Team team0_
             * where
             *     team0_.id=?
             */
            findTeam.getName(); // 이때 Team 의 select 쿼리가 날라간다.

            // JPQL N+1 테스트를 위한 초기화
            entityManager.flush();
            entityManager.clear();

            /**
             * === BEFORE JPQL TEST ===
             * Hibernate:
             *      select
             *          member0_.id as id1_0_,
             *          member0_.name as name2_0_,
             *          member0_.TEAM_ID as team_id3_0_
             *      from
             *          Member member0_
             *Hibernate:
             *      select
             *          team0_.id as id1_1_0_,
             *          team0_.name as name2_1_0_
             *      from
             *          Team team0_
             *      where
             *          team0_.id =?
             * ===AFTER JPQL TEST ===
             */
            System.out.println("=== BEFORE JPQL TEST ==="); // 즉시로딩 시 JPQL N+1 테스트
            List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();
            // Member 만 가져오려 하였지만 Team까지 추가 조회하게 된다.
            System.out.println("=== AFTER JPQL TEST ===");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
