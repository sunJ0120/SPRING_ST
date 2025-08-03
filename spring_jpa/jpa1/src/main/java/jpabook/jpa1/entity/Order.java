package jpabook.jpa1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

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
}
