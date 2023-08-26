package advancedmapping.single;

import javax.persistence.*;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // default 가 single 이라서 안적어도 된다.
//@DiscriminatorColumn // 단일 테이블 전략의 경우 해당 어노테이션이 없어도 DTYPE 생성
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
