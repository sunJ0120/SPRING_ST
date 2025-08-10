package jpabook.spring_data_jpa.repository;

import jpabook.spring_data_jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
MemberRepository는 회원 정보를 관리하는 리포지토리
- DATA JPA를 이용한 리팩터링 완료
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //해당 이름에 해당하는 회원 정보를 조회하는 메서드, data jpa를 이용하여 자동으로 구현됨
    public List<Member> findByName(String name);
}
