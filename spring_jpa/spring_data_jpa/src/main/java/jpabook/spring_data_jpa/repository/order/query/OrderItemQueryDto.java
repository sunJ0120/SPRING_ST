package jpabook.spring_data_jpa.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private String itemName; //상품명
    private int orderPrice; //주문 가격
    private int count; //주문 수량
}
