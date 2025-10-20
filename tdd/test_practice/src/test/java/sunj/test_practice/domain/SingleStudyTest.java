package sunj.test_practice.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import sunj.test_practice.tags.FindSlowTestExtension;

@TestInstance(Lifecycle.PER_CLASS) //클래스 하나당 하나의 인스턴스만 생성하도록
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ExtendWith(FindSlowTestExtension.class)
public class SingleStudyTest {
    int value = 1;

    @RegisterExtension
    static FindSlowTestExtension findSlowTest = new FindSlowTestExtension(1000L);

    @BeforeAll
    void beforeAll() {
        System.out.println("테스트 시작 전 시작");
    }

    @AfterAll
    void afterAll() {
        System.out.println("테스트 시작 후 시작");
    }

    @Order(3)
    @Test
    @DisplayName("객체 test용 심플한 테스트1")
    void simpleTest() throws Exception {
        //given

        //when

        //then
        System.out.println(this);    // @15eb5ee5
    }

    @Order(2)
    @Test
    @DisplayName("객체 test용 심플한 테스트2")
    void simpleTest2() throws Exception {
        //given

        //when

        //then
        System.out.println(this);    // @15eb5ee5
    }

    @Order(1)
    @Test
    @DisplayName("느린 테스트 만들기")
    void slow_test() throws Exception {
        //given
        Thread.sleep(1005L);

        //when

        //then
        System.out.println(this);
    }
}
