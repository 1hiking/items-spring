package org.hik.items.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/")
public class ItemRestController {


    private ItemService itemService;

    @Autowired
    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("item/")
    public List<Item> getAllItems() {
        return itemService.listItems();
    }

    @DeleteMapping("item/remove/{id:\\d+}")
    public void deleteItem(@PathVariable long id) {
        var item = itemService.findItemById(id);
        itemService.deleteItem(item.getId());

    }


    // @TODO untested
    @PutMapping("item/update/{id:\\d+}")
    public void updateItem(@PathVariable long id) {
        var item = itemService.findItemById(id);
        itemService.modifyItem(item);

    }
}
