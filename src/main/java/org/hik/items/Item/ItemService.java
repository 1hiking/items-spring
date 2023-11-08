package org.hik.items.Item;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public void addItem(Item item) {
        if (item != null) {
            itemRepository.save(item);
        }
    }

    public Optional<Item> findItemById(long id) {
        return itemRepository.findById(id);
    }

    // @TODO Should it return something? How to represent it to the user.
    public String deleteItem(long id) {
        if (findItemById(id).isEmpty()) {
            return "Entity not found";
        }
        itemRepository.deleteById(id);
        return "Success";
    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public void modifyItem(Item item) {
        findItemById(item.getId()).ifPresent(item1 -> {
            item1.setName(item.getName());
            item1.setDescription(item.getDescription());
            item1.setQuantity(item1.getQuantity());
            itemRepository.save(item1);
        });
    }

}
