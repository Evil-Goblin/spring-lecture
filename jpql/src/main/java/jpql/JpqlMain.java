package jpql;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {

    public static void simpleTest() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            entityManager.persist(member);

            // 타입 반환이 명확할때
//            TypedQuery<Member> selectMFromMemberM = entityManager.createQuery("select m from Member m", Member.class);

            // 타입 반환이 명확하지 않을 때
//            Query query = entityManager.createQuery("select m.username, m.age from Member m");

//            TypedQuery<Member> query = entityManager.createQuery("select m from Member m", Member.class);
            // 결과가 없을 때 NoResultException
            // 결과가 많을 때 NonUniqueResultException
//            Member findMember = query.getSingleResult();

            TypedQuery<Member> query = entityManager.createQuery("select m from Member m where m.username = :username", Member.class);
            query.setParameter("username", "memberA");
            List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void projection() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            // 엔티티 프로젝션
            List<Member> resultList = entityManager.createQuery("select m from Member m", Member.class)
                    .getResultList();

            // jpql의 결과도 영속성 컨텍스트에 관리된다.
            resultList.get(0).setUsername("test");

            // 엔티티 프로젝션
//            List<Team> findTeams = entityManager.createQuery("select m.team from Member m", Team.class)
            List<Team> findTeams = entityManager.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();
//            System.out.println("findTeams.get(0) = " + findTeams.get(0));

            List<Address> findAddress = entityManager.createQuery("select o.address from Order o", Address.class).getResultList();
//            System.out.println("findAddress.get(0) = " + findAddress.get(0));

            List scalaTypeList = entityManager.createQuery("select distinct m.username, m.age from Member m").getResultList();
            for (Object o : scalaTypeList) {
                Object[] result = (Object[]) o;
                System.out.println("result = " + result[0]);
                System.out.println("result = " + result[1]);
            }

            List<Object[]> scalaTypeListUsedGeneric = entityManager.createQuery("select distinct m.username, m.age from Member m").getResultList();
            for (Object[] objects : scalaTypeListUsedGeneric) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
            }

            List<MemberDTO> resultList1 = entityManager.createQuery("select distinct new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();
            for (MemberDTO memberDTO : resultList1) {
                System.out.println("memberDTO = " + memberDTO);
            }


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void paging() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                entityManager.persist(member);
            }

            entityManager.flush();
            entityManager.clear();

            List<Member> result = entityManager.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void join() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            // 내부 조인
            // select m from Member m [INNER] JOIN m.team t

            // 외부 조인
            // select m from Member m LEFT [OUTER] JOIN m.team t

            // 세타 조인
            // select count(m) from Member m, Team t where m.username = t.name

            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(team);
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

//            String query = "select m from Member m, Team t where m.username = t.name";
//            List<Member> resultList = entityManager.createQuery(query, Member.class).getResultList();
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }

            // join 대상 필터링 ( ON )
            // select m, t from Member m left join m.team t on t.name = 'A'

            // 연관관계 없는 엔티티 외부 조인
            // select m, t from Member m left join team t on m.username = t.name

            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> resultList = entityManager.createQuery(query, Member.class).getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();

    }

    public static void subQuery() {
        // select m from Member m where m.age > (select avg(m2.age) from Member m2)

        // 서브쿼리 지원 함수
        /**
         * [NOT] Exists (subquery): 서브쿼리에 결과가 존재하면 참
         *      {ALL | ANY | SOME} (subquery)
         *      ALL 모두 만족하면 참
         *      ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
         *
         * [NOT] IN (subquery): 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
         */
        // select m from Member m where exists (select t from m.team t where t.name = 'TeamA')
        // select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)
        // select m from Member m where m.team = ANY (select t from Team t)
    }

    public static void type() {
        // select m.username, 'HELLO', TRUE From Member m where m.type = jpql.MemberType.ADMIN

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

//            String query = "select m.username, 'HELLO', true from Member m where m.type = jpql.MemberType.USER";
            String query = "select m.username, 'HELLO', true from Member m where m.type = :userType";
            List<Object[]> resultList = entityManager.createQuery(query)
                    .setParameter("userType", MemberType.USER)
                    .getResultList();
            for (Object[] objects : resultList) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[0] = " + objects[1]);
                System.out.println("objects[0] = " + objects[2]);
            }

            // 상위객체로 조회
            // select i from Item i where type(i) = Book

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void caseQuery() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setUsername(null);
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

//            String query = "select m.username, 'HELLO', true from Member m where m.type = jpql.MemberType.USER";
            String query = "select " +
                                "case when m.age <= 10 then '학생요금' "+
                                    "when m.age >= 60 then '경로요금' "+
                                    "else '일반요금' "+
                                "end " +
                            "from Member m";

            query = "select coalesce(m.username, '이름 없는 회원') from Member m";
            query = "select nullif(m.username, '관리자') from Member m";
            List<String> resultList = entityManager.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            // 상위객체로 조회
            // select i from Item i where type(i) = Book

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void jpqlFunc() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setUsername("123456789");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            Member member2 = new Member();
            member2.setUsername("6543");
            member2.setAge(10);
            member2.changeTeam(team);
            member2.setType(MemberType.USER);
            entityManager.persist(member2);

            entityManager.flush();
            entityManager.clear();

            String query = "select concat('a', 'b') from Member m";
            query = "select substring(m.username, 2,3) from Member m";
            query = "select locate('de', 'abcdefg') from Member m";
            query = "select function('group_concat', m.username) from Member m";
//            query = "select size(t.members) from Team t";
//            query = "select index(t.members) from Team t";
            List<String> resultList = entityManager.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

//            List<Integer> resultList = entityManager.createQuery(query, Integer.class)
//                    .getResultList();
//            for (Integer s : resultList) {
//                System.out.println("s = " + s);
//            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void fetchjoin() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            entityManager.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            entityManager.persist(teamB);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(teamA);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            Member member2 = new Member();
            member2.setUsername("memberB");
            member2.setAge(10);
            member2.changeTeam(teamA);
            member2.setType(MemberType.USER);
            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUsername("memberC");
            member3.setAge(10);
            member3.changeTeam(teamB);
            member3.setType(MemberType.USER);
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();

            // N + 1 문제 발생
            // Team 이 지연 로딩으로 설정되어 있기 때문에 Member 조회 이후 Team을 추가로 조회하게 된다.
            // fetch join 으로 해결한다.
//            String query = "select m from Member m";

//            String query = "select m from Member m join fetch m.team";
//
//            List<Member> resultList = entityManager.createQuery(query, Member.class)
//                    .getResultList();
//
//            for (Member s : resultList) {
//                System.out.println("s = " + s + " team = " + s.getTeam());
//            }

            // 실제 쿼리에서 distinct가 되지 않는 문장도 애플리케이션 내에서 중복을 제거해준다.
            // fetch join 은 추가 쿼리 없이 한번에 다 땡겨오기 때문에 즉시 로딩이라고 볼 수 있다. ( N + 1 해소 )
            String query = "select distinct t from Team t join fetch t.members"; /*where t.name = 'teamA'";*/
            List<Team> resultList = entityManager.createQuery(query, Team.class).getResultList();
            System.out.println("resultList.size() = " + resultList.size());

            for (Team team : resultList) {
                System.out.println("team = " + team);
                for (Member teamMember : team.getMembers()) {
                    System.out.println("teamMember = " + teamMember);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void queryUsedEntity() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            entityManager.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            entityManager.persist(teamB);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(teamA);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            Member member2 = new Member();
            member2.setUsername("memberB");
            member2.setAge(10);
            member2.changeTeam(teamA);
            member2.setType(MemberType.USER);
            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUsername("memberC");
            member3.setAge(10);
            member3.changeTeam(teamB);
            member3.setType(MemberType.USER);
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();

            /**
             * select
             *     member0_.id as id1_0_,
             *     member0_.age as age2_0_,
             *     member0_.TEAM_ID as team_id5_0_,
             *     member0_.type as type3_0_,
             *     member0_.username as username4_0_
             * from
             *     Member member0_
             * where
             *     member0_.id=?
             */
            String query = "select m from Member m where m = :member";
            Member findMember = entityManager.createQuery(query, Member.class)
                    .setParameter("member", member) // member 객체를 파라미터로 넘겼지만 실제 쿼리는 id로 수행된다.
                    .getSingleResult();
            System.out.println("findMember = " + findMember);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void namedQuery() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            entityManager.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            entityManager.persist(teamB);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(teamA);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            Member member2 = new Member();
            member2.setUsername("memberB");
            member2.setAge(10);
            member2.changeTeam(teamA);
            member2.setType(MemberType.USER);
            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUsername("memberC");
            member3.setAge(10);
            member3.changeTeam(teamB);
            member3.setType(MemberType.USER);
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();

            List<Member> resultList = entityManager.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "memberA")
                    .getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void bulk() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            entityManager.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            entityManager.persist(teamB);

            Member member = new Member();
            member.setUsername("memberA");
            member.setAge(10);
            member.changeTeam(teamA);
            member.setType(MemberType.USER);
            entityManager.persist(member);

            Member member2 = new Member();
            member2.setUsername("memberB");
            member2.setAge(10);
            member2.changeTeam(teamA);
            member2.setType(MemberType.USER);
            entityManager.persist(member2);

            Member member3 = new Member();
            member3.setUsername("memberC");
            member3.setAge(10);
            member3.changeTeam(teamB);
            member3.setType(MemberType.USER);
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();

            String query = "update Member m set m.age = 20";

            int i = entityManager.createQuery(query)
                    .executeUpdate();
            System.out.println("i = " + i);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    public static void main(String[] args) {

//        projection();
//        paging();
//        join();
//        subQuery();
//        type();
//        caseQuery();
//        jpqlFunc();
//        fetchjoin();
//        queryUsedEntity();
//        namedQuery();
        bulk();
    }
}
