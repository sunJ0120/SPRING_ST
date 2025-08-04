package jpabook.jpa1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpa1.entity.*;
import jpabook.jpa1.entity.item.Book;
import jpabook.jpa1.exception.NotEnoughStockException;
import jpabook.jpa1.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    /*
    체크 사항
    1. 주문한 상품의 종류 수가 정확해야 한다.
    2. 주문한 상품의 종류 체크
    3. 주문한 상품의 수량 체크
    4. 재고가 줄었는지 체크
     */
    @Test
    void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10); //이름, 가격, 재고를 설정한다.
        int orderCount = 2; //주문 수량

        //when
        //orderService.order() 메서드를 호출하여 주문을 진행한다.
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        //then
        //1. 주문한 상품의 종류체크
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        //2. 주문한 상품의 종류체크
        assertEquals(1, getOrder.getItems().size(), "주문한 상품의 종류 수가 정확해야 한다.");
        //3. 주문한 상품의 수량 체크
        assertEquals(10000 * 2, getOrder.getTotalPrice(), "주문한 상품의 총 가격이 정확해야 한다.");
        //4. 재고가 줄었는지 체크
        assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    //이렇게 비정상 상황을 테스트하는 것도 중요하다.
    @Test
    void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10); //이름, 가격, 재고를 설정한다.
        int orderCount = 11; //주문 수량, 재고보다 많은 수량을 주문한다.
        //when
        //then
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        }, "재고 수량이 부족한 경우 예외가 발생해야 한다.");
    }

    /*
    체크 사항
    1. 주문 취소시 상태는 CANCEL이다.
    2. 주문 취소한 상품의 재고는 원복되어야 한다.
     */
    @Test
    void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);
        int orderCount = 2; //주문 수량을 두 개로 잡는다.

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId); //주문 취소를 진행한다.
        Order getOrder = orderRepository.findOne(orderId); //주문을 조회한다.

        //then
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다.");
        assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 원복되어야 한다.");
    }

    // test용 객체를 따로 생성한다.
    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        //값 타입이므로 new로 따로 생성한다.
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    // test용 객체를 따로 생성한다.
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}