package org.hik.items.Item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
@RequestMapping("/item/")
public class ItemController {

    private final ItemService itemService;


    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public ModelAndView getIndex(@AuthenticationPrincipal OidcUser principal) {
        return new ModelAndView("item/index").addObject("profile", principal.getClaims())
                .addObject("items", itemService.listItems(principal.getName()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("add")
    public ModelAndView getItemCreationForm(Model model) {
        var newItem = new Item();
        return new ModelAndView("item/add").addObject("item", newItem);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("add")
    public String processItemCreationForm(@Valid Item item,
                                          BindingResult result,
                                          @AuthenticationPrincipal OidcUser principal) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                log.error("Field error: {} - {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
            return "item/add";
        }
        item.setUserId(principal.getName());
        itemService.addItem(item);
        return "item/add";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("details/{id:\\d+}")
    public ModelAndView getDetailsOfItem(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());

        return new ModelAndView("item/details").addObject("item", item);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("update/{id:\\d+}")
    public ModelAndView getItemUpdateForm(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());

        return new ModelAndView("item/update").addObject("item", item);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("update/{id:\\d+}")
    public ModelAndView processItemUpdateForm(@PathVariable Long id,
                                              @Valid Item item,
                                              BindingResult result,
                                              @AuthenticationPrincipal OidcUser principal) {
        var itemToUpdate = itemService.findItemByIdAndUserId(id, principal.getName());
        if (itemToUpdate == null) {
            return new ModelAndView("item/update").addObject("errorMessage", "Error fetching data.");
        }

        if (result.hasErrors()) {
            return new ModelAndView("item/update").addObject("item", item);
        }

        itemService.modifyItem(item);
        return new ModelAndView("redirect:/item/");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("delete/{id:\\d+}")
    public ModelAndView getItemDeleteForm(@PathVariable Long id, @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());

        return new ModelAndView("item/delete").addObject("item", item);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("delete/{id:\\d+}")
    public String processItemDeleteForm(@PathVariable Long id,
                                        Model model,
                                        @AuthenticationPrincipal OidcUser principal) {
        var item = itemService.findItemByIdAndUserId(id, principal.getName());
        if (item == null) {
            model.addAttribute("errorMessage", "Error deleting item.");
            return "item/delete";
        }

        itemService.deleteItem(item.getId());
        return "redirect:/item/";
    }

}
