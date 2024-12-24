package org.hik.items.Item;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private PolicyFactory namePolicy;
    private PolicyFactory descriptionPolicy;
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Item addItem(Item item) {
        if (item != null) {
            namePolicy = new HtmlPolicyBuilder().toFactory();
            descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

            var replacedNewLines = item.getDescription().replace(System.lineSeparator(), "<br>");

            var safeName = namePolicy.sanitize(item.getName());
            var safeDescription = descriptionPolicy.sanitize(replacedNewLines);
            item.setDescription(safeDescription);
            item.setName(safeName);
            itemRepository.save(item);
            logger.info("Item added: {}", item);
        }
        return item;
    }

    @Transactional(readOnly = true)
    public Item findItemByIdAndUserId(long id, String userId) {
        Optional<Item> item = itemRepository.findByIdAndUserId(id, userId);
        logger.info("Item found: {}", item);
        return item.orElseThrow();
    }

    public void deleteItem(long id) {
        itemRepository.deleteById(id);
        logger.info("Item deleted: {}", id);
    }

    public List<Item> listItems(String userId) {
        logger.info("List of items: {}", itemRepository.findByUserId(userId));
        return itemRepository.findByUserId(userId);
    }

    @Transactional
    public void modifyItem(long id, String name, String description, int quantity, String userId) {
        namePolicy = new HtmlPolicyBuilder().toFactory();
        descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

        var item = findItemByIdAndUserId(id, userId);
        logger.info("Item to modify: {}", item);
        var descriptionWithNewlines = description.replace("\n", "<br>");
        var safeName = namePolicy.sanitize(name);
        var safeDescription = descriptionPolicy.sanitize(descriptionWithNewlines);

        item.setDescription(safeDescription);
        item.setName(safeName);
        item.setQuantity(quantity);
        logger.info("Item modified: {}", item);

    }

}
