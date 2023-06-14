package hello.introduction;

import hello.introduction.repository.*;
import hello.introduction.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

//    @Autowired
//    private DataSource dataSource;
    @Autowired
    private EntityManager em;

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

//    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
}
