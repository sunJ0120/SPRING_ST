package sunj.test_practice.domain;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import sunj.test_practice.tags.FastTest;
import sunj.test_practice.tags.SlowTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
    @DisplayName("조건 테스트")
    @Test
    @EnabledOnOs({OS.WINDOWS, OS.MAC}) //이걸 이용하면 OS에 맞춰서 실행 가능
//    @EnabledOnOs({OS.MAC})
//    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "피용히") //특정 패턴일때만 실행하라.
    void 조건에_따른_테스트하기() throws Exception {
        //given
        Dotenv dotenv = Dotenv.load();
        String testEnv = dotenv.get("TEST_ENV");
        System.out.println(testEnv);

        //when

        //then
        Assumptions.assumeTrue("피용히".equalsIgnoreCase(testEnv));
    }

    @FastTest
    @DisplayName("스터디 만들기 🔥")
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

    @SlowTest
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