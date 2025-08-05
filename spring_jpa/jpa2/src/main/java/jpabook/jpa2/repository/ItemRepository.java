package jpabook.jpa2.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpa2.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item); // 새 아이템인 경우, persist를 사용하여 저장해서 id를 만든다.
        } else {
            em.merge(item); // 이미 존재하는 아이템인 경우, merge를 사용하여 준영속 상태의 아이템을 병합한다.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id); // 아이템 ID로 아이템을 조회한다.
    }

    //마찬가지로, 다건 조회는 JPQL을 이용한다.
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class) // 모든 아이템을 조회하는 JPQL 쿼리
                 .getResultList();
    }
}
