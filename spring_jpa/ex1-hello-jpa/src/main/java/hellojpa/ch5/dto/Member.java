package hellojpa.ch5.dto;

/*
ch5 - 연관관계 매핑 시작 실습

Member Entity
외래키를 직접 넣는 것이 아닌, 연관관계의 객체를 참조하도록 구성한다.
 */

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
    @GeneratedValue
    @Column(name = "MEMBER_ID") // 컬럼 이름을 MEMBER_ID로 지정
    private Long id;

    private String name;

    private String city;
    private String street;
    private String zipcode;

    //Member에서 Order를 조회하는 일은 왕왕 있으므로, 양방향 매핑을 해준다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();
}
