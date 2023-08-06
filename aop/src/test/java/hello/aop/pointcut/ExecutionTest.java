package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointCut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod={}", helloMethod); // execution 은 이 정보를 통해 포인트컷 대상을 찾아낸다. (포인트컷 표현식)
    }

    @Test
    void exactMatch() { // 가장 시그니처가 같은 형식인 경우
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointCut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() { // 가장 많이 생략한 포인트컷
        pointCut.setExpression("execution(* *(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        pointCut.setExpression("execution(* hello(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1() {
        pointCut.setExpression("execution(* hel*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointCut.setExpression("execution(* *el*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointCut.setExpression("execution(* Non(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() {
        pointCut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        pointCut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactFalse() {
        pointCut.setExpression("execution(* hello.aop.*.*(..))"); // subpackage가 맞지 않아서 실패
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1() {
        pointCut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2() {
        pointCut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage3() {
        pointCut.setExpression("execution(* hello.aop..*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        pointCut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointCut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); // 부모 타입 허용
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointCut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointCut.matches(internal, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointCut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointCut.matches(internal, MemberServiceImpl.class)).isTrue();
    }

    // String 타입의 파라미터 허용
    @Test
    void argsMatch() {
        pointCut.setExpression("execution(* *(String))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 파라미터 없는 경우
    @Test
    void argsMatchNoArgs() {
        pointCut.setExpression("execution(* *())");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 정확히 하나의 파라미터만 허용, 모든 타입 허용 (파라미터 개수가 하나)
    @Test
    void argsMatchStar() {
        pointCut.setExpression("execution(* *(*))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 정확히 두개의 파라미터만 허용, 모든 타입 허용 (파라미터 개수가 둘)
    @Test
    void argsMatchStarDouble() {
        pointCut.setExpression("execution(* *(*, *))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    @Test
    void argsMatchAll() {
        pointCut.setExpression("execution(* *(..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    // (String), (String, Xxx), (String, Xxx, Xxx ...)
    @Test
    void argsMatchComplex() {
        pointCut.setExpression("execution(* *(String, ..))");
        assertThat(pointCut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
