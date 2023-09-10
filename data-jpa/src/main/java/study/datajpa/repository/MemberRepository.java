package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") // Entity의 NamedQuery와 매칭시킨다. 사실 없애도 동작한다. 베이스가 되는 entity의 NamedQuery와 알아서 매핑해주기 때문 (메소드명으로 쿼리를 생성해주기 전에 NamedQuery를 먼저 찾고 없으면 쿼리를 생성해준다.)
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // 여기서 바로 쿼리를 생성할 수 있다.(NamedQuery 는 Entity에 작성해야하는 불편함이 있다.)
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); // 컬렉션 (컬렉션은 조회 결과가 없을 경우 Empty 컬렉션으로 리턴된다. NotNull)
    Member findMemberByUsername(String username); // 단건 (단건 조회는 조회 결과가 없을 경우 Null 이 리턴된다.
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional (만약 조회 결과가 여럿인 경우 예외발생...)

    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // count 쿼리 별도 분리
    Page<Member> findCustomByAge(int age, Pageable pageable);

    // clearAutomatically 옵션을 true로 주게 되면 쿼리 이후 영속성 컨텍스트를 초기화해준다.
    @Modifying(clearAutomatically = true) // 업데이트 쿼리에는 해당 어노테이션을 꼭 넣어줘야한다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) // fetch join 한 쿼리가 나간다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) // 이런 식으로 다른 방식의 쿼리에도 적용이 가능하다.
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team")) // 이런 식으로 다른 방식의 쿼리에도 적용이 가능하다.
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) // readOnly를 사용하며 조회 결과가 영속성컨텍스트에 등록되지 않는다.
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
