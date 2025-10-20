package sunj.test_practice.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cdimascio.dotenv.Dotenv;
import java.time.Duration;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import sunj.test_practice.tags.FastTest;
import sunj.test_practice.tags.SlowTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
    @DisplayName("조건 테스트")
    @Test
    @EnabledOnOs({OS.WINDOWS, OS.MAC})
        //이걸 이용하면 OS에 맞춰서 실행 가능
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

    @DisplayName("WE GO UP")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void 반복_테스트_1(RepetitionInfo repetitionInfo) throws Exception {
        //given

        //when

        //then
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" +
                repetitionInfo.getTotalRepetitions());

    }

    @DisplayName("파라미터 테스트 1탄")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"베몬", "신곡", "매우", "좋다"})
    @NullAndEmptySource
    void 파라미터_테스트_1(String message) throws Exception {
        //given

        //when

        //then
        System.out.println(message); //여기에 @ValueSource가 들어온다.

    }

    @DisplayName("컨버터 테스트 1탄")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void 컨버터_테스트(@ConvertWith(StudyConverter.class) Study study) throws Exception {
        //given

        //when

        //then
        System.out.println(study.getLimit());
    }

    @DisplayName("컨버터 테스트 2탄")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디", "20, 스프링"})
    void 컨버터_집합_테스트(ArgumentsAccessor argumentsAccessor) throws Exception {
        //given

        //when
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));

        //then
        System.out.println(study);
    }

    @DisplayName("컨버터 테스트 3탄")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디", "20, 스프링"})
    void 컨버터_집합_커스텀_ArgumentsAggregator(@AggregateWith(AggregatorConverter.class) Study study) throws Exception {
        //given

        //when

        //then
        System.out.println(study);
    }

    static class StudyConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Study 타입만 변환이 가능합니다.");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    static class AggregatorConverter implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
                throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }
}