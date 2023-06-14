package hello.introduction;

import hello.introduction.repository.JdbcMemberRepository;
import hello.introduction.repository.MemberRepository;
import hello.introduction.repository.MemoryMemberRepository;
import hello.introduction.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
        return new JdbcMemberRepository(dataSource);
    }

//    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
