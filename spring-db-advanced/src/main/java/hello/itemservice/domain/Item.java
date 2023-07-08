package hello.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//@Table(name = "item") // 객체명과 테이블명이 같다면 생략가능
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", length = 10) // camelcase , underscore 자동 변환해주기 때문에 생략 가능
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
