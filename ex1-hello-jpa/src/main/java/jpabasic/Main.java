package jpabasic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Team teamb = new Team();
            teamb.setName("TeamB");

            entityManager.persist(teamb);

            Member member1 = new Member();
            member1.setTeam(teamb);
            member1.setName("MemberB");

            entityManager.persist(member1);

            entityManager.flush();
            entityManager.clear();

            Team findTeam = entityManager.find(Team.class, teamb.getId());
            List<Member> members = findTeam.getMembers();
            for (Member member : members) {
                System.out.println("member = " + member);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
