package sunj.coffeee.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long price;

    // quantity가 재고라면
    private Long stockQuantity;
    // 상품 설명
    private String description;
    // 이미지
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ItemStatus status; // AVAILABLE, SOLD_OUT, DISCONTINUED
}
