package org.hik.items.Item;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(value = "/")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("item/")
    public String index(@Nonnull Model model) {
        model.addAttribute("items", itemService.listItems());
        return "index";
    }


    @GetMapping("item/add")
    public String addItemForm(@Nonnull Model model) {
        model.addAttribute("item", new Item());
        return "add";
    }

    @PostMapping("item/add")
    public String addItemSubmit(@ModelAttribute Item item, @Nonnull Model model) {
        model.addAttribute("item", item);
        itemService.addItem(item);
        return "result";
    }

    @GetMapping("item/{id:\\d+}")
    public String getItem(@PathVariable("id") long id, @Nonnull Model model) {
        var itemOpt = itemService.findItemById(id);
        if (itemOpt.isEmpty()) {
            throw new NoSuchElementException("No such Item");
        }
        Item item = itemOpt.get();
        model.addAttribute("item", item);
        return "item";
    }


    // @TODO Not implemented yet on the visual side, it works.  See service layer
    @DeleteMapping("item/remove/{id:\\d+}")
    public void deleteItem(@PathVariable long id) {
        var itemOpt = itemService.findItemById(id);
        if (itemOpt.isEmpty()) {
            throw new NoSuchElementException("No such item");
        }
        var item = itemOpt.get();
        itemService.deleteItem(item.getId());

    }

    @GetMapping("/item/update/{id:\\d+}")
    public String updateItemForm(@PathVariable long id, Model model) {
        var itemOpt = itemService.findItemById(id);
        if (itemOpt.isEmpty()) {
            throw new NoSuchElementException("No such item");
        }
        var item = itemOpt.get();
        model.addAttribute("item",item);

        return "add";
    }

    // @TODO untested
    @PutMapping("item/update/{id:\\d+}")
    public void updateItem(@PathVariable long id) {
        var itemOpt = itemService.findItemById(id);
        if (itemOpt.isEmpty()) {
            throw new NoSuchElementException("No such item");
        }
        var item = itemOpt.get();
        itemService.modifyItem(item);

    }
}
