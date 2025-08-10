package jpabook.spring_data_jpa.entity.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jpabook.spring_data_jpa.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter @Setter
public class Album extends Item {
    private String artist; // 아티스트
    private String etc; // 기타 정보
}
