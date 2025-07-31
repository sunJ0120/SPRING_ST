package hellojpa.ch9;

import hellojpa.ch9.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
@Setter
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent; // 부모 카테고리

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); // 자식 카테고리들

    //연관관계 메서드
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this); // 자식 카테고리의 부모를 현재 카테고리로 설정
    }
}
