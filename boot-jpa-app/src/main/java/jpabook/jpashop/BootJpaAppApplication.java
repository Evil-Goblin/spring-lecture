package jpabook.jpashop;

//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BootJpaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootJpaAppApplication.class, args);
    }

    @Bean
    public TestDateInit testDateInit(MemberRepository memberRepository) {
        return new TestDateInit(memberRepository);
    }

//    @Bean
//    Hibernate5Module hibernate5Module() {
//        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
//        return hibernate5Module;
//    }

}
