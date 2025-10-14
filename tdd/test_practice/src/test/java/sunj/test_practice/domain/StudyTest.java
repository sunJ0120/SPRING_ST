package sunj.test_practice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
    @DisplayName("스터디 만들기 🔥")
    @Test
    void create_new_study() throws Exception {
        //given
        int limit = 1;

        //when
        Study study = new Study(limit);

        //then
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),  //Object expected, Object actual, Supplier<String>
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    @Test
    @DisplayName("ThreadLocal에서 assertTimeout vs assertTimeoutPreemptively 차이")
    void ThreadLocal_test() throws Exception {
        //given
        Study study = new Study(10);
        study.threadLocal.set(study); //독립된 thread에서 실행

        //when

        //then
        // assertTimeout vs assertTimeoutPreemptively
        assertTimeout(Duration.ofMillis(100), () -> {
            Study retrieved = study.threadLocal.get();
            assertNotNull(retrieved, "같은 스레드 이므로, ThreadLocal안에 값이 있다.");
            assertEquals(10, retrieved.getLimit());
        });

        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            Study retrieved = study.threadLocal.get();
            assertNull(retrieved, "다른 스레드 이므로, ThreadLocal안에 값이 없다.");
        });
    }
}