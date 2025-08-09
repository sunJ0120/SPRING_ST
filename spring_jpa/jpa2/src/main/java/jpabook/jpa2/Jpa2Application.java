package jpabook.jpa2;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Jpa2Application {

    //엔티티를 직접 노출할때 사용하는 모듈이다.
    @Bean
    Hibernate5JakartaModule  hibernate5Module() {
        Hibernate5JakartaModule hibernate5Module = new Hibernate5JakartaModule();
        //강제 지연 로딩 설정
        //FORCE_LAZY_LOADING을 사용하면 강제로 지연 로딩이 가능하다.
        hibernate5Module.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING,
                true);
        return hibernate5Module;
    }

    public static void main(String[] args) {
        SpringApplication.run(Jpa2Application.class, args);
    }
}
