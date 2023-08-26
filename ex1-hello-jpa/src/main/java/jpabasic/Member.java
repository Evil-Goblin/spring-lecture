package jpabasic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString // 주의!! Team에도 ToString을 적용하면 순환 호출이 발생한다.
@Getter
@Setter
//@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

//    @Column(name = "TEAM_ID")
//    private Long teamId;
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
