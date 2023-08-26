package advancedmapping.table_per_class;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class TablePerClassMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Movie movie = new Movie();
            movie.setDirector("a");
            movie.setActor("b");
            movie.setName("c");
            movie.setPrice(1000);

            entityManager.persist(movie); // Item , Movie 에 둘다 insert가 날아간다.

            entityManager.flush();
            entityManager.clear(); // select 테스트 하기 전 영속성 컨텍스트 비우기

            /**
             * select
             *         item0_.id as id1_2_0_,
             *         item0_.name as name2_2_0_,
             *         item0_.price as price3_2_0_,
             *         item0_.artist as artist1_0_0_,
             *         item0_.author as author1_1_0_,
             *         item0_.isbn as isbn2_1_0_,
             *         item0_.actor as actor1_3_0_,
             *         item0_.director as director2_3_0_,
             *         item0_.clazz_ as clazz_0_
             *     from
             *         ( select
             *             id,
             *             name,
             *             price,
             *             artist,
             *             null as author,
             *             null as isbn,
             *             null as actor,
             *             null as director,
             *             1 as clazz_
             *         from
             *             Album
             *         union
             *         all select
             *             id,
             *             name,
             *             price,
             *             null as artist,
             *             author,
             *             isbn,
             *             null as actor,
             *             null as director,
             *             2 as clazz_
             *         from
             *             Book
             *         union
             *         all select
             *             id,
             *             name,
             *             price,
             *             null as artist,
             *             null as author,
             *             null as isbn,
             *             actor,
             *             director,
             *             3 as clazz_
             *         from
             *             Movie
             *     ) item0_
             * where
             *     item0_.id=?
             */
            Item findItem = entityManager.find(Item.class, movie.getId()); // TABLE_PER_CLASS 전략의 경우 부모 타입으로 find 수행시 union 을 통한 복잡한 쿼리가 생성된다.
            System.out.println("findItem = " + findItem);


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
