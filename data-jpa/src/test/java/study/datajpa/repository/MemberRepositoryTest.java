package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository repository;

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
    public void findByUsernameAndAgeGreaterThan() {
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


}
