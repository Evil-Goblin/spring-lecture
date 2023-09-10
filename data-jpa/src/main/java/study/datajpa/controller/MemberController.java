package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") // Domain class converter
    public String findMember2(@PathVariable("id") Member member) { // spring data jpa 가 알아서 매핑해서 가져온다.
        return member.getUsername();
    }

    @GetMapping("/members") // query param http://..../members?page=0&size=3&sort=id,desc&sort=username,desc (default는 사이즈20
    public Page<Member> list(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members2") // default 설정을 직접 입력
    public Page<Member> list2(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/membersdto") // Dto가 Entity를 의존하는 것은 괜찮다. 하지만 Entity가 Dto를 의존하는 것은 지양하자.
    public Page<MemberDto> listdto(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> all = memberRepository.findAll(pageable);
        return all.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
