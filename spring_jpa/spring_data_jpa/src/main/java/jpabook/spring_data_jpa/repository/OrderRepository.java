package jpabook.spring_data_jpa.repository;

import jpabook.spring_data_jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/*
OrderRepository는 주문 정보를 관리하는 리포지토리입니다.

- 복잡한 쿼리에서 명세 기능을 이용하기 위해 JpaSpecificationExecutor를 상속받습니다.
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
