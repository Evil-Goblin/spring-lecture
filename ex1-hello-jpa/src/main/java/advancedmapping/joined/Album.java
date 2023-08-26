package advancedmapping.joined;

import javax.persistence.Entity;

//@Entity
// @DiscriminatorValue("A") // 부모 테이블에 입력되는 DTYPE 값 변경
public class Album extends Item{

    private String artist;
}
