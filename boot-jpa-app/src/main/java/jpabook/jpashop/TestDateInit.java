package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TestDateInit {

    private final MemberRepository repository;

    // Transactional 어노테이션을 사용하지 않으면 No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call 에러 발생
    // jpa 는 transaction 안에서 동작하는데 (영속성 컨텍스트) transaction이 없어서 발생하는 문제이다.
    @Transactional // repository 에 Transactional 을 붙이는 것이 아닌 service 에 붙이다 보니 repository를 바로 호출하는 경우 transaction이 없어서 jpa.persist 를 수행할 수 없다.
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        Member memberA = new Member();
        memberA.setName("memberA");
        memberA.setAddress(new Address("cityA", "streetA", "zipcodeA"));

        Member memberB = new Member();
        memberB.setName("memberB");
        memberB.setAddress(new Address("cityB", "streetB", "zipcodeB"));
        repository.save(memberA);
        repository.save(memberB);
    }
}
