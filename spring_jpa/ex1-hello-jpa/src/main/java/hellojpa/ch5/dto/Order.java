package hellojpa.ch5.dto;

/*
ch5 - 연관관계 매핑 시작 실습

Order Entity
외래키를 직접 넣는 것이 아닌, 연관관계의 객체를 참조하도록 구성한다.
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS") // 'Order'는 Java 예약어이므로 테이블 이름을 'ORDERS'로 지정
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID") // 컬럼 이름을 ORDER_ID로 지정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // 외래키 컬럼 이름 지정
    private Member member;

    //Order에서 OrderItem을 조회하는 일은 왕왕 있으므로, 양방향 매핑을 해준다.
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate; //주문 시간을 저장한다.

    //Enumerated 를 이용해서 STRING 타입으로 주문 상태를 저장한다.
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태를 저장

    //연관관계 편의 메서드 - 양방향 매핑을 위해 사용한다.
    public void setMember(Member member) {
        this.member = member;
        if (member != null) { //member가 이미 연관관계가 존재하여, null이 아닌 경우에만
            member.getOrders().add(this); //Member의 orders에 현재 Order를 추가
        }
    }

    //연관관계 편의 메서드 - 양방향 매핑을 위해 사용한다.
    //orderItem을 추가할 때마다 Order의 orderItems에 추가하고, OrderItem의 order를 현재 Order로 설정한다.
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); //OrderItem의 order를 현재 Order로 설정
    }
}
