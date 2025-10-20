package sunj.test_practice.member;

/**
 * 클래스 이름: MemberService
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-19
 */

import java.lang.reflect.Member;
import java.util.Optional;
import sunj.test_practice.domain.Study;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

    void notify(Member member);
}
