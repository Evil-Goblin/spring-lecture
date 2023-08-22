package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public void persistContextCache01() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setId(1L);
            member.setName("helloJPA");

            entityManager.persist(member);

            // 만약 find가 직접 db를 통해 수행된다면 query로그가 남아야 한다.
            System.out.println(" === BEFORE FIND === ");
            entityManager.find(Member.class, 1L);
            System.out.println(" === AFTER FIND === ");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public void persistContextCache02() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // Cache 되기 때문에 두 find 함수에 대해서 각각 db를 호출하지 않는다.
            // 실제 db 수행이 한번만 수행된다.
            Member findMember1 = entityManager.find(Member.class, 1L);
            Member findMember2 = entityManager.find(Member.class, 1L);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public void persistSample() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // 비영속 ( jpa와 무관계 )
            Member member = new Member();
            member.setId(1L);
            member.setName("helloJPA");

            System.out.println(" === BEFORE PERSIST === ");
            // 영속 ( 영속성컨텍스트를 통해 member 객체 관리 )
            entityManager.persist(member); // 이때 db에 저장이 되지 않는다.
            System.out.println(" === AFTER PERSIST === "); // 만약 쿼리가 수행됐다면 이 로그 사이에 출력되어야한다.

            // 영속성 컨텍스트에서 분리 ( 준영속 상태 )
            // entityManager.detach(member);

            System.out.println(" === BEFORE COMMIT === ");
            transaction.commit(); // 여기서 쿼리가 수행된다.
            System.out.println(" === AFTER COMMIT === ");
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

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
