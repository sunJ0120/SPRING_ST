package jpabook.spring_data_jpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static jpabook.spring_data_jpa.domain.OrderSpec.memberNameLike;
import static jpabook.spring_data_jpa.domain.OrderSpec.orderStatusEq;

@Getter
@Setter
public class OrderSearch {
    private String memberName;
    //회원 이름
    private OrderStatus orderStatus;//주문 상태[ORDER, CANCEL]

    //자신이 가진 검색 조건으로 Specification을 생성
    public Specification<Order> toSpecification() {
        return memberNameLike(memberName)
                .and(orderStatusEq(orderStatus));
    }
}
