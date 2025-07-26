package hellojpa.ch9.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Address {
    private String city; // 도시
    private String street; // 거리
    private String zipcode; // 우편번호

    //Equals와 HashCode를 재정의하여 Address 객체의 동등성 비교를 가능하게 해야 한다...
}
