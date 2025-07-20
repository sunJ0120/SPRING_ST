package hellojpa.ch7.item;

import jakarta.persistence.*;

/**
 * Item - 상품을 나타내는 추상 클래스
 *
 * 이 클래스는 상품의 공통 속성을 정의하며, 실제 상품 엔티티들은 이 클래스를 상속받아 구현된다.
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE") // 구분 컬럼 이름을 DTYPE으로 지정
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "ITEM_ID") // 컬럼 이름을 ITEM_ID로 지정
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
