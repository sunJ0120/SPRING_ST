package jpabook.jpa1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id") // 소문자_대쉬 전략 사용
    private Long id;

    //여기서는 주로 주문에서 배송을 참조하기 때문에 연관관계의 주인은 Order로 둔다.
    @OneToOne(mappedBy = "delivery", fetch = LAZY) // 일대일 관계, 연관관계의 주인은 Order
    private Order order; // 주문

    @Embedded // 임베디드 타입
    private Address address; // 배송지 주소

    @Enumerated(EnumType.STRING) // Enum 타입은 String으로 저장
    private DeliveryStatus status; // 배송 상태
}
