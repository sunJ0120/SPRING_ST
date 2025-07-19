package hellojpa.ch5.dto;

/*
ch5 - 연관관계 매핑 시작 실습

Item Entity
Item에서 OrderItem을 조회하는 일은 거의 없으므로, 양방향 매핑을 하지 않고 단방향만 진행한다.
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ITEM") // 테이블 이름을 ITEM으로 지정
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue
    @Column(name="ITEM_ID") // 컬럼 이름을 ITEM_ID로 지정
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
