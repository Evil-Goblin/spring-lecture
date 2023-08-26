package advancedmapping.joined;

import javax.persistence.*;

//@Entity
@Inheritance(strategy = InheritanceType.JOINED) // default 는 싱글테이블 전략
@DiscriminatorColumn // 부모 테이블에 DTYPE 칼럼 추가 ( 자식 테이블 정보 )
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
