package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberTeamDto {

    private String username;
    private String teamName;

    @QueryProjection // 안타깝게도 lombok과 적용시점이 다르기 때문에 lombok의 생성자에는 적용할 수 없다. 무조건 수동으로 생성자를 만들어야 한다.
    public MemberTeamDto(String username, String teamName) {
        this.username = username;
        this.teamName = teamName;
    }
}
