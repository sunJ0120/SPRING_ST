package jpabook.spring_data_jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") // 소문자_대쉬 전략 사용
    private Long id;

    private String name; // 카테고리명

    /*
        다대다 관계를 설정한다.
        카테고리와 아이템은 다대다 관계이므로, 중간 테이블을 사용하여 관계를 설정한다.
        @ManyToMany 어노테이션을 사용하고, @JoinTable 어노테이션으로 중간 테이블을 정의한다.
     */

    @ManyToMany
    @JoinTable(name = "category_item", // 중간 테이블 이름
               joinColumns = @JoinColumn(name = "category_id"), // 카테고리 외래키
               inverseJoinColumns = @JoinColumn(name = "item_id")) // 아이템 외래키, 반대편 외래키를 설정한다.
    private List<Item> items = new ArrayList<>(); // 아이템 목록

    /*
        부모 카테고리와 자식 카테고리의 관계를 설정.
        결국 category라는 이름만 같지, 서로 다른 엔티티라고 보면 된다.
        이렇게 하면 부모 카테고리와 자식 카테고리를 같은 엔티티로 관리할 수 있다.
     */
    @ManyToOne(fetch = FetchType.LAZY) // 부모 카테고리와의 관계
    @JoinColumn(name = "parent_id") // 부모 카테고리 외래키
    private Category parent; // 부모 카테고리

    @OneToMany(mappedBy = "parent") // 자식 카테고리 목록
    private List<Category> children = new ArrayList<>(); // 자식 카테고리 목록

    //==연관관계 편의 메서드==
    public void addChildCategory(Category child) {
        this.children.add(child); // this로 형용되는 parents 에게 자식 카테고리를 추가
        child.setParent(this); // 자식 카테고리의 부모를 현재 카테고리로 설정
    }
}
