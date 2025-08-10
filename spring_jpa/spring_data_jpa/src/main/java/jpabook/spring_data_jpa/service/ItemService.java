package jpabook.spring_data_jpa.service;

import jpabook.spring_data_jpa.entity.Item;
import jpabook.spring_data_jpa.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item); // 아이템을 저장하는 메서드
    }

    public List<Item> findItems() {
        return itemRepository.findAll(); // 모든 아이템을 조회하는 메서드
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId).orElse(null); // 특정 아이템을 ID로 조회하는 메서드
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     *
     * - find로 찾는다.
     * - set으로 세팅하면, 자동으로 영속성 관리 된다.
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity){
        Item item = itemRepository.findById(id).orElse(null);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }
}
