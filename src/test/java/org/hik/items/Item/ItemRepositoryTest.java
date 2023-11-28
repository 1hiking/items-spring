package org.hik.items.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.saveAllAndFlush(List.of(
                new Item().setName("Item 1").setDescription("Cool description").setQuantity(12).createItem(),
                new Item().setName("Item 2").setDescription("Nice description").setQuantity(11).createItem(),
                new Item().setName("Item 3").setDescription("Epic description").setQuantity(10).createItem()));
    }

    @Test
    void findAllItems() {
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(3);
    }

}