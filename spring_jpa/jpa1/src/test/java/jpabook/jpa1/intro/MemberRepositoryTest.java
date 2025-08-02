package jpabook.jpa1.intro;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");
        Long savedId = memberRepository.save(member); //save() 메서드를 통해서 member 객체를 영속화하고, 식별자(id)를 반환받는다.

        //when
        Member findMember = memberRepository.find(savedId); //find() 메서드를 통해서 영속화된 객체를 조회한다.

        //then
        assertEquals(member.getUsername(), findMember.getUsername()); //저장된 객체와 조회된 객체의 username이 동일한지 검증한다.
        assertEquals(member, findMember); //저장된 객체와 조회된 객체가 동일한지 검증한다. (entity의 동일성 비교)
    }
}