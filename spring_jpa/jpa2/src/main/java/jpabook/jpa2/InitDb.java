package jpabook.jpa2;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpa2.entity.*;
import jpabook.jpa2.entity.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DB에 조회용 샘플 데이터를 넣기 위한 코드
 *
 * - userA
 *   - JPA1 BOOK
 *   - JPA2 BOOK
 * - userB
 *   - SPRING1 BOOK
 *   - SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {
    //애플리케이션 로딩 시점에 이를 호출하기 위해 @RequiredArgsConstructor 선언한다.
    private final InitService initService;

    //초기화 메서드
    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        //UserA가 산 Book들의 연관관계를 설정해서 db에 저장하기 위함이다.
        public void dbInit1() {

            //member 객체 생성 및 영속화
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            //book1 객체 생성
            Book book1 = createBook("JPA1 BOOK", 10000, 10);
            em.persist(book1);

            //book2 객체 생성
            Book book2 = createBook("JPA2 BOOK", 20000, 10);
            em.persist(book2);

            //OrderItem 생성및 Order 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1,book1.getPrice(),1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2,book2.getPrice(),2);

            //Order 생성
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "진주", "2", "2222");
            em.persist(member);

            //book1 객체 생성
            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            //book2 객체 생성
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            //OrderItem 생성및 Order 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1,book1.getPrice(),1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2,book2.getPrice(),2);

            //Order 생성
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        //== 생성 메서드들 ==//
        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }
    }
}
