package org.hik.items.Item;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String index(@Nonnull Model model) {
        model.addAttribute("items", itemService.listItems());
        return "index";
    }

    @GetMapping("item/count")
    @ResponseBody
    public int getItemCount() {
        return itemService.listItems().size();
    }


    @GetMapping("item/add")
    public String addItemForm(@Nonnull Model model) {
        model.addAttribute("item", new Item());

        return "itemForm";
    }

    @GetMapping("item/details/{id:\\d+}")
    @ResponseBody
    public String getItemIndex(@PathVariable(value = "id") long id) {
        var item = itemService.findItemById(id);
        var name = item.getName();
        var description = item.getDescription();
        var quantity = item.getQuantity();
        String response = String.format("<p>Name: %s</p> \n <p>Description: %s \n <p>Quantity: %d</p>", name, description, quantity);

        return response;
    }


    @GetMapping("/item/update/{id:\\d+}")
    public String updateItemForm(@PathVariable long id, @Nonnull Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "itemUpdateForm";
    }

    @PostMapping(value = "item/add")
    public String addItemSubmit(@ModelAttribute Item item) {
        Item returnedItem = itemService.addItem(item);

        return "redirect:/";
    }

    @DeleteMapping("item/remove/{id:\\d+}")
    @ResponseBody
    public String  deleteItem(@PathVariable long id) {
        var item = itemService.findItemById(id);
        itemService.deleteItem(item.getId());

        return "";
    }

    // @TODO untested
    @PutMapping("item/update/{id:\\d+}")
    public void updateItem(@RequestBody Item item, @PathVariable long id) {
        var itemToUpdate = itemService.findItemById(id);
        if (itemToUpdate != null) {
            itemService.modifyItem(item);
        }
    }

}
