package hellojpa.ch7;

import hellojpa.ch7.entity.BaseEntity;
import hellojpa.ch7.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ORDER_ITEM") // 테이블 이름을 ORDER_ITEM으로 지정
@Getter
@Setter
public class OrderItem extends BaseEntity {
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
