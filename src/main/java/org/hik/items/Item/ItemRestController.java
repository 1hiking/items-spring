package org.hik.items.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemRestController {

    private final ItemService itemService;

    @Autowired
    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("item/count")
    public int getItemCount() {
        return itemService.listItems().size();
    }

    @GetMapping("item/details/{id:\\d+}")
    public String getItemIndex(@PathVariable(value = "id") long id) {
        var item = itemService.findItemById(id);
        var name = item.getName();
        var description = item.getDescription();
        var quantity = item.getQuantity();

        return String.format("<p>Name: %s</p> \n <p>Description: %s \n <p>Quantity: %d</p>", name, description, quantity);
    }


    @DeleteMapping("item/remove/{id:\\d+}")
    public ResponseEntity<String> deleteItem(@PathVariable long id) {
        var item = itemService.findItemById(id);
        itemService.deleteItem(item.getId());

        return ResponseEntity.ok().body("");
    }

    @PutMapping("item/update/{id:\\d+}")
    public ResponseEntity<String> updateItem(@PathVariable long id, @RequestParam String name, @RequestParam String description, @RequestParam int quantity) {
        var itemToUpdate = itemService.findItemById(id);
        if (itemToUpdate != null) {
            itemService.modifyItem(id, name, description, quantity);
            return ResponseEntity.ok().header("HX-Redirect", "/").body("");
        }
        return ResponseEntity.notFound().build();
    }
}
