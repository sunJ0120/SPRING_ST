package jpabook.spring_data_jpa.api;

import jpabook.spring_data_jpa.entity.Order;
import jpabook.spring_data_jpa.entity.OrderItem;
import jpabook.spring_data_jpa.entity.OrderSearch;
import jpabook.spring_data_jpa.repository.OrderQueryRepository;
import jpabook.spring_data_jpa.repository.OrderRepository;
import jpabook.spring_data_jpa.repository.order.query.OrderFlatDto;
import jpabook.spring_data_jpa.repository.order.query.OrderItemQueryDto;
import jpabook.spring_data_jpa.repository.order.query.OrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연로딩이 필요하다.
 * - 양방향 연관관계 문제
 *
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용 X)
 * - 트랜잭션 안에서 지연 로딩이 필요하다.
 *
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용 O)
 * - 페이징 시에는 N 부분을 포기해야 한다. (대신에 BatchSize 옵션을 주면, N -> 1 쿼리로 변경이 가능하다.)
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     *
     * - hibernate5Module 등록, LAZY = NULL 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        // 지연 로딩을 강제로 하기 위해서 FOR문을 돈다.
        for (Order order : all) {
            // 여기서 order => member와 order => delivery는 지연로딩이므로, 프록시가 존재한다.
            order.getMember().getName(); //Lazy 로딩을 강제로 실행 (초기화)
            order.getDelivery().getAddress(); //Lazy 로딩을 강제로 실행 (초기화)

            List<OrderItem> orderItems = order.getItems(); //orderItems는 Lazy 로딩이므로, 프록시가 존재한다.
            orderItems.stream().forEach(o -> o.getItem().getName()); //getName()을 호출하여 Lazy 로딩을 강제로 실행 (초기화)
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // 지연 로딩을 강제로 하기 위해서 FOR문을 돈다.
        // 엔티티를 직접 노출하면 안된다. 그러므로 엔티티 말고 dto로 변환해야 한다.
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        // 엔티티를 직접 노출하면 안된다. 그러므로 엔티티 말고 dto로 변환해야 한다.
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    /**
     * V3.1. 엔티티를 조회해서 DTO로 변환 페이징 고려
     *
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화 한다.
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize를 사용하여 최적화한다.
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        // 엔티티를 직접 노출하면 안된다. 그러므로 엔티티 말고 dto로 변환해야 한다.
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        // V4.1. OrderQueryRepository를 사용하여 DTO로 변환
        // 컬렉션은 별도로 조회
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private String orderDate;
        private String orderStatus;
        private String address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate().toString();
            this.orderStatus = order.getStatus().name();
            this.address = order.getDelivery().getAddress().toString();

            orderItems = order.getItems().stream()
                    .map(orderItems -> new OrderItemDto(orderItems))
                    .toList();
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName(); //상품명
            this.orderPrice = orderItem.getOrderPrice(); //주문가격
            this.count = orderItem.getCount(); //주문수량
        }
    }
}
