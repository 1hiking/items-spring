package org.hik.items.Item;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
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
    public String getItemIndex(@PathVariable(value = "id") long id, @Nonnull Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "item";
    }


    @GetMapping("/item/update/{id:\\d+}")
    public String updateItemForm(@PathVariable long id, @Nonnull Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "add";
    }

}
