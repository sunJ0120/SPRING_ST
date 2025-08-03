package jpabook.jpa1.service;

import jpabook.jpa1.entity.Member;
import jpabook.jpa1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    //생성자 주입 방식으로 MemberRepository를 주입받는다.
    private final MemberRepository memberRepository;

    //회원 가입 메서드
    //회원 가입의 경우, 트랜잭션을 readOnly가 아닌, 재정의해서 데이터베이스에 변경 사항을 반영한다.
    @Transactional
    public Long join(Member member) {
        //회원 가입 시 중복 회원 검증
        validateDuplicateMember(member);
        //회원 정보를 저장
        memberRepository.save(member);
        return member.getId();
    }

    //중복 회원 검증 메서드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //전체 회원 조회 메서드
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //한 회원 조회 메서드
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
