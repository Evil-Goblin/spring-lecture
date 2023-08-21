package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // insert
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("helloA");
//
//            entityManager.persist(member);

            // select
//            Member findMember = entityManager.find(Member.class, 1L);
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            for (Member member : result) {
                System.out.println("member.getId() = " + member.getId());
                System.out.println("member.getName() = " + member.getName());
            }

//            System.out.println("findMember Id = " + findMember.getId());
//            System.out.println("findMember Name = " + findMember.getName());

            // delete
//            entityManager.remove(findMember);

            // update
//            findMember.setName("helloJpa");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
