package proxyrelation.cascade;

import proxyrelation.lazyload.Member;
import proxyrelation.lazyload.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CascadeMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Parent parent = new Parent();
            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            // 각 값들을 insert하기 위해 각각을 영속시켜야한다.(cascade 를 사용하지 않은 경우)
            entityManager.persist(parent);

            // Cascade 를 사용하면 child를 영속시키지 않아도 연쇄로 영속시켜준다.
//            entityManager.persist(child1);
//            entityManager.persist(child2);

            entityManager.flush();
            entityManager.clear();

            System.out.println("=== ORPHAN REMOVE ===");
            Parent findParent = entityManager.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0); // 제거된 child를 child 테이블에서도 삭제

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
