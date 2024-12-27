package org.hik.items.Item;

import lombok.extern.slf4j.Slf4j;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    @Value("${upload.dir}")
    private String uploadDir;
    private PolicyFactory namePolicy;
    private PolicyFactory descriptionPolicy;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void addItem(Item item) {
        if (item != null) {
            namePolicy = new HtmlPolicyBuilder().toFactory();
            descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

            var replacedNewLines = item.getDescription().replace(System.lineSeparator(), "<br>");

            var safeName = namePolicy.sanitize(item.getName());
            var safeDescription = descriptionPolicy.sanitize(replacedNewLines);
            item.setDescription(safeDescription).setName(safeName);
            itemRepository.save(item);
            log.info("Item added: {}", item);
        }
    }

//    public void addImageToItem(MultipartFile file) {
//        Path uploadPath = Paths.get(uploadDir);
//        try {
//            Files.createDirectories(uploadPath);
//            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            Path filePath = uploadPath.resolve(fileName);
//            file.transferTo(filePath.toFile());
//            item.setImageURL(uploadDir + fileName);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to save file", e);
//        }
//    }

    @Transactional(readOnly = true)
    public Item findItemByIdAndUserId(long id, String userId) {
        Optional<Item> item = itemRepository.findByIdAndUserId(id, userId);
        log.info("Item found: {}", item);
        return item.orElseThrow();
    }

    public void deleteItem(long id) {
        itemRepository.deleteById(id);
        log.info("Item deleted: {}", id);
    }

    public List<Item> listItems(String userId) {
        log.info("List of items: {}", itemRepository.findByUserId(userId));
        return itemRepository.findByUserId(userId);
    }

    @Transactional
    public void modifyItem(Item itemToBeModified) {
        namePolicy = new HtmlPolicyBuilder().toFactory();
        descriptionPolicy = new HtmlPolicyBuilder().allowElements("br").toFactory();

        var itemFetched = findItemByIdAndUserId(itemToBeModified.getId(), itemToBeModified.getUserId());
        log.info("Item to modify: {}", itemFetched);

        var descriptionWithNewlines = itemToBeModified.getDescription().replace("\n", "<br>");
        var safeName = namePolicy.sanitize(itemToBeModified.getName());
        var safeDescription = descriptionPolicy.sanitize(descriptionWithNewlines);

        itemFetched.setDescription(safeDescription).setName(safeName).setQuantity(itemToBeModified.getQuantity());

        log.info("Item modified: {}", itemFetched);

    }

}
