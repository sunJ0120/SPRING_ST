package hellojpa.ch9;

import hellojpa.ch9.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Category - Item 다대다 관계를 연결하기 위한 엔티티
 */
@Entity
@Getter
@Setter
public class CategoryItem {
    @Id @GeneratedValue
    @Column(name = "CATEGORY_ITEM_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
