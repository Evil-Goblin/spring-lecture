package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") // Entity의 NamedQuery와 매칭시킨다. 사실 없애도 동작한다. 베이스가 되는 entity의 NamedQuery와 알아서 매핑해주기 때문 (메소드명으로 쿼리를 생성해주기 전에 NamedQuery를 먼저 찾고 없으면 쿼리를 생성해준다.)
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // 여기서 바로 쿼리를 생성할 수 있다.(NamedQuery 는 Entity에 작성해야하는 불편함이 있다.)
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
