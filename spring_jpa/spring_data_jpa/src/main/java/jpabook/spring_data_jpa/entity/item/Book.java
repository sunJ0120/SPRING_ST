package jpabook.spring_data_jpa.entity.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jpabook.spring_data_jpa.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {
    private String author; // 저자
    private String isbn; // ISBN
}
