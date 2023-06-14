package hello.introduction;

import hello.introduction.repository.MemberRepository;
import hello.introduction.repository.MemoryMemberRepository;
import hello.introduction.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SpringConfig {

//    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

//    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
