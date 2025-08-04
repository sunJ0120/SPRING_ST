package jpabook.jpa1.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpa1.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/*
OrderRepository는 주문 정보를 관리하는 리포지토리입니다.

- 필요한 기능
1. 주문을 저장하는 기능 (em.persist 사용)
2. 주문을 ID로 조회하는 기능 (em.find 사용)
3. 주문을 검색하는 기능 (JPQL 사용)
 */

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) { ... }
}
