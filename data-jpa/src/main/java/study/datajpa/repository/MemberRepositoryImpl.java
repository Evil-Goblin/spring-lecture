package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor // spring data jpa의 이름에 Impl 을 붙이는 이름 규약을 갖는다.
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        // 여기에 구현한 메소드를 spring data jpa 가 실행시켜준다.
        // querydsl 과 같이 jpa나 jpql 만으로 해결하기 힘든 경우 사용하는 것이 좋다.
        System.out.println("MemberRepositoryImpl.findMemberCustom");
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
