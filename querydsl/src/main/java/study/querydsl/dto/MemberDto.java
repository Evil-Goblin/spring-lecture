package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    @QueryProjection // 사용시 Q객체가 생성된다. 굉장히 좋은 방법이지만 QueryDsl에 대한 의존성이 생기게된다...
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
