package hellojpa;

import hellojpa.dto.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/*
JPA에서 merge하는 것의 예시를 살펴본다.
 */
public class ExamMergeMain {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static void main(String[] args) {
        createMember("sspure123", "용히");
    }

    static Member createMember(String id, String username){
        //영속성 컨텍스트1 시작!
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction tx1 = em1.getTransaction();
        tx1.begin();

        Member member = new Member();
        member.setId(id);
        member.setUsername(username); //member update를 위한 준비

        em1.persist(member); //insert를 위한 영속성 컨텍스트에 저장
        tx1.commit(); //commit으로 반영

        em1.close(); //영속성 컨텍스트1 종료

        return member;
    }

    static void mergeMember(Member member){
        //영속성 컨텍스트2 시작
        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();

        tx2.begin();
        Member mergeMember = em2.merge(member);
        tx2.commit();

        //준영속 상태
        System.out.println("member = " + member.getUsername()); //준영속 상태에서도 set이 반영은 되다만, db에 반영되지는 않는다.

        //영속 상태
        System.out.println("mergeMember = " + mergeMember.getUsername());

        System.out.println("em2 contains member = " + em2.contains(member));
        System.out.println("em2 contains member = " + em2.contains(mergeMember));
    }
}
