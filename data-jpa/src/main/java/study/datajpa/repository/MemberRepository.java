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
}
