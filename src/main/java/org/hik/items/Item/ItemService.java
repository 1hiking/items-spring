package org.hik.items.Item;

import jakarta.transaction.Transactional;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private PolicyFactory namePolicy;
    private PolicyFactory descriptionPolicy;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(Item item) {
        if (item != null) {
            namePolicy = new HtmlPolicyBuilder().toFactory();
            descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

            item.getDescription().replace("\n", "<br>");

            var safeName = namePolicy.sanitize(item.getName());
            var safeDescription = descriptionPolicy.sanitize(item.getDescription());
            item.setDescription(safeDescription);
            item.setName(safeName);
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
        namePolicy = new HtmlPolicyBuilder().toFactory();
        descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

        var item = findItemById(id);

        item.getDescription().replace("\n", "<br>");
        var safeName = namePolicy.sanitize(item.getName());
        var safeDescription = descriptionPolicy.sanitize(item.getDescription());

        item.setDescription(safeDescription);
        item.setName(safeName);
        item.setQuantity(quantity);

    }

}
