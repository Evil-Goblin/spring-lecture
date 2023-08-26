package advancedmapping.single;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SingleStrategyMain {

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

            Movie findMovie = entityManager.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
