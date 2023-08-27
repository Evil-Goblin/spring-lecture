package valuetype;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class ValueTypeMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setName("memberA");
            member.setHomeAddress(new Address("a", "a", "a"));

            member.getFavoriteFoods().add("foodA");
            member.getFavoriteFoods().add("foodB");
            member.getFavoriteFoods().add("foodC");

            // 값타입컬렉션이었던 AddressHistory 를 일대다 단방향매핑 엔티티로 변경함에 따라 주석처리
//            member.getAddressHistory().add(new Address("b", "b", "b"));
//            member.getAddressHistory().add(new Address("c", "c", "c"));
            member.getAddressHistory().add(new AddressEntity("b", "b", "b"));
            member.getAddressHistory().add(new AddressEntity("c", "c", "c"));

            // insert memeber
            // insert addresshistory
            // insert addresshistory
            // insert favorite food
            // insert favorite food
            // insert favorite food
            // 총 6번의 쿼리가 생성된다.
            // 값 타입은 생명주기가 Member에 종속된다.
            // 흡사 Cascade.All + orphan remove 와 같다.
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            System.out.println("=== select ===");
            // Member 만을 가져온다.
            // 값 컬렉션은 기본 지연로딩이다.(@ElementCollection)
            Member findMember = entityManager.find(Member.class, member.getId());

            System.out.println("=== AddressHistroy ===");
            // 값타입컬렉션이었던 AddressHistory 를 일대다 단방향매핑 엔티티로 변경함에 따라 주석처리
            // 여기서 addresshistory select 쿼리가 호출
//            List<Address> addressHistory = findMember.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address = " + address);
//            }
            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            for (AddressEntity addressEntity : addressHistory) {
                System.out.println("addressEntity = " + addressEntity);
            }

            System.out.println("=== Favorite Food ===");
            // 여기서 favorite food select 쿼리가 호출
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            // Address 변경 (불변객체이기 때문에 새로 만들어서 넣어줘야한다.)
            findMember.setHomeAddress(new Address("d", "d", "d"));
            // 값타입컬렉션이었던 AddressHistory 를 일대다 단방향매핑 엔티티로 변경함에 따라 주석처리
//            findMember.getAddressHistory().add(findMember.getHomeAddress());
            findMember.getAddressHistory().add(new AddressEntity(findMember.getHomeAddress()));

            // foodC -> foodD
            findMember.getFavoriteFoods().remove("foodC");
            findMember.getFavoriteFoods().add("foodD");

            // equals 가 같아야 지워진다.(오버라이드 필수)
            // 테이블의 memberid에 해당하는 값들을 전부 삭제하고 다시 넣는다.
            /**
             * Hibernat
             *     delete
             *         from
             *             ADDRESS
             *         where
             *             MEMBER_ID=?
             * Hibernate:
             *     insert
             *         into
             *             ADDRESS
             *             (MEMBER_ID, city, street, zipcode)
             *         values
             *             (?, ?, ?, ?)
             * Hibernate:
             *     insert
             *         into
             *             ADDRESS
             *             (MEMBER_ID, city, street, zipcode)
             *         values
             *             (?, ?, ?, ?)
             */
            // 값타입컬렉션이었던 AddressHistory 를 일대다 단방향매핑 엔티티로 변경함에 따라 주석처리
//            findMember.getAddressHistory().remove(new Address("c", "c", "c"));
            findMember.getAddressHistory().remove(new AddressEntity("c", "c", "c"));

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
