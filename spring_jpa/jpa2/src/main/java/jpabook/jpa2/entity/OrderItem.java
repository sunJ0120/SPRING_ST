package jpabook.jpa2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id") //소문자_대쉬 전략 사용
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 외래키
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키
    private Item item;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    /*
    생성 메서드

    - 복잡한 생성 같은 경우는, 이렇게 생성 메서드가 하나 있으면 좋다.
    - ordeItem 역시 많은 연관관계가 걸려 있으므로, 생성은 이 한 부분에서 전담하도록 한다.
    - 생성 지점을 변경하면, 이것만 변경하면 되기 때문에 그 부분이 중요하다.
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고 감소
        return orderItem;
    }

    //==비즈니스 로직==//
    /*
    주문 취소

    - order에서 주문 취소할 때 요청하는 method
    - 여기서 수량을 원복시키는 메소드를 정의한다.
    - item의 재고를 증가시키는 로직을 가져와서 작성한다.
    - 이렇게 각 역할을 맡은 도메인끼리 연결되어 있는게 DDD 설계이다.
     */
    public void cancel() {
        getItem().addStock(count); // 재고 증가
    }

    //==조회 로직==//
    /*
    주문 상품 전체 가격 조회

    - order에서 사용하는 것이다.
    - 수량 * 가격으로 전체 가격을 조회한다.
     */
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
