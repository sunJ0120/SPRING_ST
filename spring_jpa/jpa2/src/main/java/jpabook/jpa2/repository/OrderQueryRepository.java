package jpabook.jpa2.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpa2.repository.order.query.OrderFlatDto;
import jpabook.jpa2.repository.order.query.OrderItemQueryDto;
import jpabook.jpa2.repository.order.query.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회;
     * Query : 루트 1번, 컬렉션 N번
     * 단건 조회에서 많이 사용하는 방식이다.
     *
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한 번에 조회)
        List<OrderQueryDto> result = findOrders();
        //루프를 돌면서 컬렉션을 추가한다. (추가 쿼리를 실행한다.)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    /**
     * 1:N 관계(컬렉션)을 제외한 나머지를 한번에 조회
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpa2.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * 1:N 관계(컬렉션)를 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpa2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /**
     * 최적화
     * Query : 루트 1번, 컬렉션 1번
     * 데이터를 한꺼번에 처리할 때 많이 사용하는 방식이다.
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        //루트 조회 (toOne 코드를 모두 한 번에 조회)
        List<OrderQueryDto> result = findOrders();

        //orderItem 컬렉션을 MAP 한 방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemsMap(toOrderIds(result));

        //루프를 돌면서 컬렉션 추가 (추가 쿼리 를 실행하지 않는다.)
        //map에서 orderId로 묶어서 한번에 조회한 orderItem 컬렉션을 추가한다.
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }
    //in절을 사용하여 orderId로 묶어서 한번에 조회하는 방식을 사용한다.
    private Map<Long, List<OrderItemQueryDto>> findOrderItemsMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpa2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpa2.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.items oi " +
                        "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
