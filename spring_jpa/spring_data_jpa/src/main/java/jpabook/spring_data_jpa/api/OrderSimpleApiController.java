package jpabook.spring_data_jpa.api;

import jpabook.spring_data_jpa.entity.Address;
import jpabook.spring_data_jpa.entity.Order;
import jpabook.spring_data_jpa.entity.OrderSearch;
import jpabook.spring_data_jpa.entity.OrderStatus;
import jpabook.spring_data_jpa.repository.OrderRepository;
import jpabook.spring_data_jpa.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.spring_data_jpa.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * xToOne (ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order => Member
 * Order => Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     *
     * - Hibernate5Module 등록, LAZY = NULL 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     * - 항상 패턴이 그랬지만, 엔티티를 직접 노출하면 절대 안된다!!
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        //FORCE-LAZY-LOADING 하지 않고, 지연 로딩을 강제로 하기 위해서 FOR문을 돈다.
        for(Order order : all){
            //여기서 order => member와 order => delivery는 지연로딩이므로, 프록시가 존재한다.
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용 X)
     *
     * - 단점
     *   - 지연로딩으로 쿼리를 N번 호출한다.
     */
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());

        return new Result(result);
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환한다. (fetch join 사용)
     *
     * - fetch join으로 쿼리를 한 번 호출한다.
     * 참고 : fetch join에 대한 자세한 내용은 JPA 기본편을 참고한다. (참고로, 매우 중요하다.)
     * - v2와 구조 자체는 완전히 같으나, 쿼리가 몇 방 나가느냐가 완전히 다르다.
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());

        return result;
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     *
     * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     * - 여기서는 orderSimpleQueryRepository를 따로 빼서 method를 조회하도록 한다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }

    // 매핑 결과를 한 번더 묶는다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    //return용 dto
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화 : 먼저 식별자를 이용해서 영속성 컨텍스트를 찾는다.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 로딩
        }
    }
}


