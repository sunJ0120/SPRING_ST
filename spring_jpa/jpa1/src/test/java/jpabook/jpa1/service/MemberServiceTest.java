package jpabook.jpa1.service;

import jpabook.jpa1.entity.Member;
import jpabook.jpa1.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("oh");

        //when
        Long saveId = memberService.join(member);

        //then
        //memberRepository에서 해당 ID로 회원을 찾았을 때, 같은지 확인한다.
        Assertions.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        //이렇게 중복된 이름을 넣으려고 한다고 해보자.
        Member member1 = new Member();
        member1.setName("oh");

        Member member2 = new Member();
        member2.setName("oh");
        //when
        memberService.join(member1);

        //then
        //memberService.join을 호출했을 때, 중복된 회원 이름으로 인해 예외가 발생해야 한다.
        Assertions.assertThrows(IllegalStateException.class, () ->
                memberService.join(member2));

    }
}