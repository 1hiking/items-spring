package org.hik.items.Item;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public Item addItem(Item item) {
        if (item != null) {
            itemRepository.save(item);
        }
        return item;
    }

    public Item findItemById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElseThrow();
    }

    public void deleteItem(long id) {
        itemRepository.deleteById(id);

    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public void modifyItem(long id, String name, String description, int quantity) {

        var item = findItemById(id);

        item.setName(name);
        item.setDescription(description);
        item.setQuantity(quantity);

    }

}
