package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository repository;
    @Autowired TeamRepository teamRepository;
    @Autowired EntityManager em;

    @Test
    void memberTest() {
        Member member = new Member("memberA");
        Member savedMember = repository.save(member);

        Member findMember = repository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");

        repository.save(memberA);
        repository.save(memberB);

        Member findMemberA = repository.findById(memberA.getId()).get();
        Member findMemberB = repository.findById(memberB.getId()).get();

        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> all = repository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = repository.count();
        assertThat(count).isEqualTo(2);

        repository.delete(memberA);
        repository.delete(memberB);

        long deletedCount = repository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        repository.save(m1);
        repository.save(m2);
        List<Member> result =
                repository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);

        List<Member> result = repository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);

        List<String> usernameList = repository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member memberA = new Member("MemberA", 10);
        memberA.changeTeam(teamA);
        repository.save(memberA);

        List<MemberDto> memberDto = repository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);

        List<Member> byNames = repository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }

    @Test
    void testReturnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);

        List<Member> aaa = repository.findListByUsername("AAA");
        for (Member member : aaa) {
            System.out.println("member = " + member);
        }

        Member aaa1 = repository.findMemberByUsername("AAA");
        System.out.println("aaa1 = " + aaa1);

        Optional<Member> aaa2 = repository.findOptionalByUsername("AAA");
        System.out.println("aaa2.get() = " + aaa2.get());
    }

    @Test
    void page() throws Exception {
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 10));
        repository.save(new Member("member3", 10));
        repository.save(new Member("member4", 10));
        repository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Page<Member> page = repository.findByAge(10, pageRequest);

        List<Member> content = page.getContent(); //조회된 데이터
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    void slice() throws Exception {
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 10));
        repository.save(new Member("member3", 10));
        repository.save(new Member("member4", 10));
        repository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Slice<Member> page = repository.findSliceByAge(10, pageRequest); // 얘는 size + 1 씩 가져온다.

        List<Member> content = page.getContent(); //조회된 데이터
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    void customPage() throws Exception {
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 10));
        repository.save(new Member("member3", 10));
        repository.save(new Member("member4", 10));
        repository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Page<Member> page = repository.findCustomByAge(10, pageRequest);

        List<Member> content = page.getContent(); //조회된 데이터
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    void bulkUpdate() {
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 19));
        repository.save(new Member("member3", 20));
        repository.save(new Member("member4", 21));
        repository.save(new Member("member5", 40));

        int resultCount = repository.bulkAgePlus(20);

        List<Member> member5 = repository.findByUsername("member5");
        Member member = member5.get(0);

        assertThat(resultCount).isEqualTo(3);

//        clearAutomatically 옵션을 통해 강제 초기화를 한 경우
//        assertThat(member.getAge()).isNotEqualTo(41); // bulk 연산시 영속성 컨텍스트와 무관하게 수행되기 때문에 영속성컨텍스트와 실제 db값이 달라지게 된다.
//
//        em.clear(); // 영속성 컨텍스트를 초기화 해줘야 db의 값을 가져올 수 있다.
//
//        member5 = repository.findByUsername("member5");
//        member = member5.get(0);

        assertThat(member.getAge()).isEqualTo(41);
    }

    @Test
    void findMemberLazy() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("MemberA", 10, teamA);
        Member memberB = new Member("MemberB", 10, teamB);
        repository.save(memberA);
        repository.save(memberB);

        em.flush();
        em.clear();

        List<Member> all = repository.findAll();
//        List<Member> all = repository.findMemberFetchJoin();
        for (Member member : all) {
            System.out.println("member = " + member.getUsername());
            System.out.println("team = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        Member memberA = new Member("memberA", 10);
        repository.save(memberA);
        em.flush();
        em.clear();

        Member findMember = repository.findReadOnlyByUsername("memberA");
        findMember.setUsername("member2");
        em.flush();
    }

    @Test
    void lock() {
        Member memberA = new Member("memberA", 10);
        repository.save(memberA);
        em.flush();
        em.clear();

        /**
         * select
         *     m1_0.member_id,
         *     m1_0.age,
         *     m1_0.team_id,
         *     m1_0.username
         * from
         *     member m1_0
         * where
         *     m1_0.username=? for update
         */ // for update 쿼리가 추가된다.
        List<Member> result = repository.findLockByUsername("memberA");
    }
}
