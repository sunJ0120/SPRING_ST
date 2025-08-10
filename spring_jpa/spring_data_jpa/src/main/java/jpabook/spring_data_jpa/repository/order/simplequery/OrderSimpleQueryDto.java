package jpabook.spring_data_jpa.repository.order.simplequery;

import jpabook.spring_data_jpa.entity.Address;
import jpabook.spring_data_jpa.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
}
