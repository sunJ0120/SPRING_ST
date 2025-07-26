package hellojpa.ch9;

import hellojpa.ch9.entity.Address;
import hellojpa.ch9.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DELIVERY")
@Setter
@Getter
public class Delivery extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

//    private String city;
//    private String street;
//    private String zipcode;

    // 이 부분을 값 타입으로 변경한다.
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //배송 상태를 저장
}
