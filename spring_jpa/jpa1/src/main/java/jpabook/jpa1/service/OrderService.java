package jpabook.jpa1.service;

import jpabook.jpa1.entity.*;
import jpabook.jpa1.repository.ItemRepository;
import jpabook.jpa1.repository.MemberRepository;
import jpabook.jpa1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    주문 생성 메서드

    - 주문은 write 해야 하는 것이므로, @Transactional 어노테이션을 사용해서 덮어준다.
    - 결국, order를 생성하는 것이므로 order의 생성에 필요한 것들을 생성하는 과정이다.
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보를 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); //원래는 배송지 주소를 입력 받아야 하는데, 예제를 간단하게 하기 위해 그냥 회원의 주소를 받는다.
        delivery.setStatus(DeliveryStatus.READY); // 배송 준비 상태로 설정

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /*
    주문 취소 메서드

    - 주문 취소는 write 해야 하는 것이므로, @Transactional 어노테이션을 사용해서 덮어준다.
    - order.cancel(); 하면 내부에서 수량 원복까지 진행된다. 즉 service는 연결다리 역할만 하면 되는 것이다.
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }
}
