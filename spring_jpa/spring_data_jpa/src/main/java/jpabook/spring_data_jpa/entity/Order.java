package jpabook.spring_data_jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // 연관관계의 주인
    @JoinColumn(name = "member_id") // 외래키
    private Member member; // 회원 식별자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<OrderItem>();

    //일대다 관계에 연관관계 주인은 Delivery 엔티티이다.
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // 외래키
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING) // Enum 타입은 String으로 저장
    private OrderStatus status; // 주문 상태

    /*
    ==연관관계 편의 메서드==

    - 양방향의 경우, 연관관계 편의 메서드가 필요하다.
    - 연관관계 편의 메서드는, 조회를 많이 하는 곳에 작성한다.
    - 여기서는 member, delivery가 양방향 이다.
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // 양방향 연관관계 설정
        //member가 가진 orders에 현재 order를 추가한다.
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this); // 양방향 연관관계 설정
        //delivery가 가진 order에 현재 order를 추가한다.
    }

    public void addOrderItem(OrderItem orderItem) {
        items.add(orderItem);
        orderItem.setOrder(this); // 양방향 연관관계 설정
        //orderItem이 가진 order에 현재 order를 추가한다.
    }

    /*
    생성 메서드

    - 복잡한 생성 같은 경우는, 이렇게 생성 메서드가 하나 있으면 좋다.
    - order에 많은 연관관계가 걸려 있으므로, 연관관계를 싹 걸어서 동작한다.
    - 생성 지점을 변경하면, 이것만 변경하면 되기 때문에 그 부분이 중요하다.
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER); //order 상태이므로, order로 강제한다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /*
    주문 취소

    - 수량은 orderItem이 가지고 있는 값이고, 재고 수량을 원복해주는게 중요하므로
    - orderItem.cancel();을 통해 수량을 복원해준다.
     */
    public void cancel() {
        //이미 배송 상태가 완료 되었을 경우, 취소할 수 없도록 한다.
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송이 완료된 주문은 취소할 수 없습니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : items) {
            orderItem.cancel(); // 주문 아이템 취소 & 수량 원복
        }
    }

    //==조회 로직==//
    /*
    전체 주문 가격 조회

    - 전체 주문 가격을 조회해야 할 때가 있으므로, 이 메서드를 넣어준다.
    - 주문 아이템의 총 가격은 수량 * 객단가를 해야 하므로, 이 부분 역시 orderItem에서 계산한다.
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : items) {
            totalPrice += orderItem.getTotalPrice(); // 각 주문 아이템의 총 가격을 더한다.
        }
        return totalPrice;
    }
}
