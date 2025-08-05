package jpabook.jpa2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpa2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
MemberRepository는 회원 정보를 관리하는 리포지토리입니다.

- 이 클래스는 JPA를 사용하여 회원 정보를 데이터베이스에 저장하고 조회하는 기능을 제공합니다.
 */
@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    // 회원 정보를 저장하는 메서드, persist 사용
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 정보를 찾는 메서드, find 사용
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //모든 회원 정보를 조회하는 메서드, createQuery로 JPQL 쿼리를 실행
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }

    //해당 이름에 해당하는 회원 정보를 조회하는 메서드, createQuery & setParameter로 JPQL 쿼리를 실행
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
