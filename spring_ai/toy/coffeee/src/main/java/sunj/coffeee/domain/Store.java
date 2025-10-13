package sunj.coffeee.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String president; //대표

    // 스토어는 여러개의 메뉴를 가질 수 있으므로 양방향 매핑
    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    // 주소
    private String address;

    // 위도, 경도
    private Double latitude;
    private Double longitude;

    // 오픈 시간, 마감 시간
    private LocalDateTime openTime;
    private LocalDateTime closeTime;

    @Enumerated(EnumType.STRING)
    private StoreStatus status; //OPEN, CLOSED, TEMPORARILY_CLOSED


}
