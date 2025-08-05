package jpabook.jpa2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") //소문자_대쉬 전략 사용
    private Long id;
    private String name;

    @Embedded //임베디드 타입
    private Address address;

    // 회원과 주문은 1:N 관계
    @OneToMany(mappedBy = "member") //mappedBy는 연관관계의 주인이 아니다.
    private List<Order> orders = new ArrayList<Order>();
}
