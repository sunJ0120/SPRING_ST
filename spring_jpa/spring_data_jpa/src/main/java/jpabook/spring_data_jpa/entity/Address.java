package jpabook.spring_data_jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
임베디드 타입

- 임베디드 타입은, Immutable해야 하므로, Setter가 없어야 한다.
 */

@Embeddable
@Getter
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {}
}
