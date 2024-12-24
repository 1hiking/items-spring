package org.hik.items.Item;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;


    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String getIndex(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("profile", principal.getClaims());
        model.addAttribute("items", itemService.listItems(principal.getName()));
        return "item/index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("add")
    public String getItemCreationForm(Model model) {
        var newItem = new Item().createItem();
        model.addAttribute("item", newItem);
        return "item/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("add")
    public String processItemCreationForm(@Valid Item item, BindingResult result, @AuthenticationPrincipal OidcUser principal) {
        if (result.hasErrors()) {
            return "item/add";
        }
        item.setUserId(principal.getName());
        itemService.addItem(item);
        return "redirect:/item/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("details/{id:\\d+}")
    public String getDetailsOfItem(@PathVariable Long id, Model model, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());
        model.addAttribute("item", item);

        return "item/details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("update/{id:\\d+}")
    public String getItemUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());
        model.addAttribute("item", item);

        return "item/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("update/{id:\\d+}")
    public String processItemUpdateForm(@PathVariable Long id, @Valid Item item, BindingResult result, Model model, @AuthenticationPrincipal OidcUser principal) {
        var itemToUpdate = itemService.findItemByIdAndUserId(id, principal.getName());
        if (itemToUpdate == null) {
            model.addAttribute("errorMessage", "Error fetching data.");
            return "item/update";
        }

        if (result.hasErrors()) {
            model.addAttribute("item", item);
            return "item/update";
        }

        itemService.modifyItem(id, item.getName(), item.getDescription(), item.getQuantity(), principal.getName());
        return "redirect:/item/";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("delete/{id:\\d+}")
    public String getItemDeleteForm(@PathVariable Long id, Model model, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());
        model.addAttribute("item", item);

        return "item/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("delete/{id:\\d+}")
    public String processItemDeleteForm(@PathVariable Long id, Model model, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());
        if (item == null) {
            model.addAttribute("errorMessage", "Error deleting item.");
            return "item/delete";
        }

        itemService.deleteItem(item.getId());
        return "redirect:/item/";
    }

}
