package org.hik.items.Item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item/")
public class ItemController {

    private final ItemService itemService;


    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String getIndex(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("profile",principal.getClaims());
        model.addAttribute("items", itemService.listItems());
        return "item/index";
    }

    @GetMapping("count")
    public int getItemCount(Model model) {
        return itemService.listItems().size();
    }


    @GetMapping("add")
    public String getItemCreationForm(Model model) {
        model.addAttribute("item", new Item().createItem());
        return "item/add";
    }

    @PostMapping("add")
    public String processItemCreationForm(@Valid Item item, BindingResult result) {
        if (result.hasErrors()) {
            return "itemForm";
        }

        itemService.addItem(item);
        return "redirect:/item/";
    }

    @GetMapping("details/{id:\\d+}")
    public String getDetailsOfItem(@PathVariable Long id, Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "item/details";
    }

    @GetMapping("update/{id:\\d+}")
    public String getItemUpdateForm(@PathVariable Long id, Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "item/update";
    }

    @PostMapping("update/{id:\\d+}")
    public String processItemUpdateForm(@PathVariable Long id, @Valid Item item, BindingResult result, Model model) {
        var itemToUpdate = itemService.findItemById(id);
        if (itemToUpdate == null) {
            model.addAttribute("errorMessage", "Error fetching data.");
            return "item/update";
        }

        if (result.hasErrors()) {
            model.addAttribute("item", item);
            return "item/update";
        }

        itemService.modifyItem(id, item.getName(), item.getDescription(), item.getQuantity());
        return "redirect:item/index";

    }

    @GetMapping("delete/{id:\\d+}")
    public String getItemDeleteForm(@PathVariable Long id, Model model) {
        var item = itemService.findItemById(id);
        model.addAttribute("item", item);

        return "item/delete";
    }

    @PostMapping("delete/{id:\\d+}")
    public String processItemDeleteForm(@PathVariable Long id, Model model) {
        var item = itemService.findItemById(id);
        if (item == null) {
            model.addAttribute("errorMessage", "Error deleting item.");
            return "item/delete";
        }

        itemService.deleteItem(item.getId());
        return "redirect:/item/";
    }

}
