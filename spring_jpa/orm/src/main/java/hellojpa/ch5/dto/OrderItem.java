package hellojpa.ch5.dto;

/*
ch5 - 연관관계 매핑 시작 실습

OrderItem Entity
외래키를 직접 넣는 것이 아닌, 연관관계의 객체를 참조하도록 구성한다.
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ORDER_ITEM") // 테이블 이름을 ORDER_ITEM으로 지정
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ITEM_ID") // 컬럼 이름을 ORDER_ITEM_ID로 지정
    private Long id;

    @ManyToOne
    @JoinColumn(name="ITEM_ID") // 외래키 컬럼 이름 지정
    private Item item;

    @ManyToOne
    @JoinColumn(name="ORDER_ID") // 외래키 컬럼 이름 지정
    private Order order;

    private int orderPrice;
    private int count;
}
