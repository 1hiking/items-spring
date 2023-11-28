package org.hik.items.Item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void addItem() {
        var item = new Item().setName("<p>Item 1</p>").setDescription("Cool" + System.lineSeparator() + "description").setQuantity(12).createItem();
        when(itemService.addItem(item)).thenReturn(item);

        var resultItem = itemService.addItem(item);
        assertEquals("Item 1", resultItem.getName());
        assertEquals("Cool<br />description", resultItem.getDescription());
        assertEquals(12,item.getQuantity());
    }

    @Test
    void findItemById() {
    }

    @Test
    void deleteItem() {
    }

    @Test
    void listItems() {
    }

    @Test
    void modifyItem() {
    }
}