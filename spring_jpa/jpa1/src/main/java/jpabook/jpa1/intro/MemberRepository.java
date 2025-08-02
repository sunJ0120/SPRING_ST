package jpabook.jpa1.intro;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    /*
    Command 와 Query 분리 (CQRS) 원칙

    - save() 는 “데이터를 저장하는(커맨드)” 역할만 수행하고, 그 결과로 식별자(id)만 돌려줌으로써 커맨드와 쿼리(조회)를 분리
    - 저장 로직(save)은 순수하게 영속화 + 식별자 생성 까지만 책임지고
    - 조회 로직(find)은 데이터를 가져오는 책임만 갖게 된다.
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id); //이걸 통해서 .find로 원하는 객체를 찾을 수 있다.
    }
}
