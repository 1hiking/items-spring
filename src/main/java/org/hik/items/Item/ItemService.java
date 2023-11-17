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

    public Item findItemById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElseThrow();
    }

    // @TODO Should it return something? How to represent it to the user.
    public String deleteItem(long id) {
        itemRepository.deleteById(id);
        return "Success";
    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public void modifyItem(Item itemToModify) {

        var item = findItemById(itemToModify.getId());

        item.setName(itemToModify.getName());
        item.setDescription(itemToModify.getDescription());
        item.setQuantity(itemToModify.getQuantity());

    }

}
