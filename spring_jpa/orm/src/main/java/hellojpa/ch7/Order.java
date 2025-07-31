package hellojpa.ch7;

import hellojpa.ch7.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID") // 컬럼 이름을 ORDER_ID로 지정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // 외래키 컬럼 이름 지정
    private Member member; //주문 회원

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @OneToOne
    @JoinColumn(name = "DELIVERY_ID") // 외래키 컬럼 이름 지정
    private Delivery delivery; //배송 정보

    private Date orderDate; //주문 시간을 저장한다.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태를 저장

    //연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        if (member != null) { //member가 이미 연관관계가 존재하여, null이 아닌 경우에만
            member.getOrders().add(this); //Member의 orders에 현재 Order를 추가
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); //OrderItem의 order를 현재 Order로 설정
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        if (delivery != null) { //delivery가 이미 연관관계가 존재하여, null이 아닌 경우에만
            delivery.setOrder(this); //Delivery의 order를 현재 Order로 설정
        }
    }
}
