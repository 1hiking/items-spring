package org.hik.items.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void addItemForm() throws Exception {
        this.mockMvc.perform(get("/item/add")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updateItemForm() throws Exception {
        this.mockMvc.perform(get("/item/update/")).andDo(print()).andExpect(status().isOk());


    }

    @Test
    void addItemSubmit() throws Exception {
        this.mockMvc.perform((post("/item/add")
                        .param("name", "itemTest"))
                        .param("description", "descriptionTest")
                        .param("quantity", "12"))
                .andExpect(status().isOk());
    }
}