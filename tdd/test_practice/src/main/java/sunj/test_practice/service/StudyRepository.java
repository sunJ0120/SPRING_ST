package sunj.test_practice.service;

import org.springframework.data.jpa.repository.JpaRepository;
import sunj.test_practice.domain.Study;

/**
 * 클래스 이름: StudyRepository
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-19
 */
public interface StudyRepository extends JpaRepository<Study, Long> {

}
