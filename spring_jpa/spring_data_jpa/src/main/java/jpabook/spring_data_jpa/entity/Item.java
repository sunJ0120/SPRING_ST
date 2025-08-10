package jpabook.spring_data_jpa.entity;

import jakarta.persistence.*;
import jpabook.spring_data_jpa.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글 테이블 전략임을 명시한다.
@DiscriminatorColumn(name = "dtype") // 구분 컬럼을 지정한다. (dtype)
@Getter
@Setter
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id") // 소문자_대쉬 전략 사용
    private Long id;

    private String name; // 상품명
    private int price; // 가격
    private int stockQuantity; // 재고 수량

    @ManyToMany(mappedBy = "items") // 다대다 관계, 연관관계의 주인이 아니다. 여기서는 카테고리를 연관관계 주인으로 둠.
    List<Category> categories = new ArrayList<>(); // 카테고리

    //==비즈니스 로직==//
    // 재고 수량을 관리하는 비즈니스 로직을 추가한다.
    // 엔티티 자체가 할 수 있는 일은, 엔티티 안에 넣는 것이 좋다. -> DDD
    public void addStock(int quantity) {
        this.stockQuantity += quantity; // 재고 수량 증가
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity; // 재고 수량 감소
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock"); // 재고 부족 예외 처리
        }
        this.stockQuantity = restStock; // 재고 수량 업데이트
    }
}
