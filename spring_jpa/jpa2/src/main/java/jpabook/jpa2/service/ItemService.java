package jpabook.jpa2.service;

import jpabook.jpa2.entity.Item;
import jpabook.jpa2.repository.ItemRepository;
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
        return itemRepository.findOne(itemId); // 특정 아이템을 ID로 조회하는 메서드
    }
}
