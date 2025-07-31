package hellojpa.ch5.dto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Ch5Main {
    //항상 엔티티 매니저 팩토리로 -> 엔티티 매니저를 생성 -> 트랜잭션을 획득하고 커밋, 반납한다
    public static void main(String[] args) {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        //트랜잭션 획득
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin(); //트랜잭션 시작
            logic(em);  //비즈니스 로직 실행
            tx.commit(); //트랜잭션 커밋
        } catch (Exception e){
            tx.rollback(); //실패시 롤백
        } finally {
            em.close(); //엔티티 매니저 - 종료 (매니저가 파생이니까 먼저 종료)
        }
        emf.close(); //엔티티 매니저 팩토리 - 종료
    }

    public static void logic(EntityManager em){
        // 비즈니스 로직을 여기에 작성
        // 주문한 회원을 객체 그래프로 탐색 order -> member
        Order order = em.find(Order.class, 1);
        Member member = order.getMember(); //주문한 회원, 참조 사용

        // 주문한 상품 하나를 객체 그래프로 탐색 order -> orderItem -> item
        OrderItem orderItem = order.getOrderItems().get(0); // 첫 번째 주문 아이템
        Item item = orderItem.getItem(); // 주문 아이템에 해당하는 상품
    }
}
