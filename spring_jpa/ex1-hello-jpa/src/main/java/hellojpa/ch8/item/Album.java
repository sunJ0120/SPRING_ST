package hellojpa.ch8.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item {
    private String artist; // 아티스트
    private String etc; // 기타 정보
}
