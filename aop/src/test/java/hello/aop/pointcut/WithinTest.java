package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;

@Slf4j
public class WithinTest {

    AspectJExpressionPointcut pointCut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void withinExact() {
        pointCut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSTar() {
        pointCut.setExpression("within(hello.aop.member.*Service*)");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSubPackage() {
        pointCut.setExpression("within(hello.aop..*)");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타겟의 타입에만 직접 적용, 부모타입 사용 불가")
    void withinSuper() { // within 은 부모타입에 적용 불가
        pointCut.setExpression("within(hello.aop.member.MemberService)");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
