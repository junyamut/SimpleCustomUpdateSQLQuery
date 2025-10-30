package xyz.joseyamut.updatequerybuilder.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.domain.exception.ResourceNotFoundException;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodCategoryController.class)
@ExtendWith(SpringExtension.class)
class FoodCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FoodCategoryService foodCategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFoodCategoryById_shouldReturnCategory() throws Exception {
        FoodCategory category = new FoodCategory();
        category.setId(1);
        category.setName("Fruits");

        when(foodCategoryService.get(1)).thenReturn(category);

        mockMvc.perform(get("/food-category/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fruits"));
    }

    @Test
    public void getFoodCategories_shouldReturnList() throws Exception {
        FoodCategory cat1 = new FoodCategory();
        cat1.setId(1);
        cat1.setName("Fruits");
        FoodCategory cat2 = new FoodCategory();
        cat2.setId(2);
        cat2.setName("Vegetables");
        List<FoodCategory> categories = Arrays.asList(cat1, cat2);

        when(foodCategoryService.getList("id", 0, 5)).thenReturn(categories);

        mockMvc.perform(get("/food-category/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fruits"))
                .andExpect(jsonPath("$[1].name").value("Vegetables"));
    }

    @Test
    public void createFoodCategory_shouldReturnCreated() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setName("Dairy");
        input.setDescription("Milk products");
        input.setGroupId(10);
        input.setCreatedBy("Johnny Moo");
        input.setIsActive(true);
        FoodCategoryEntity entity = new FoodCategoryEntity();
        entity.setId(10);
        entity.setName("Dairy");
        entity.setDescription("Milk products");
        entity.setGroupId(10);
        entity.setCreatedBy("Johnny Moo");
        entity.setCreatedOn(Timestamp.valueOf("2025-10-01 10:20:43"));
        entity.setIsActive(true);
        FoodCategory created = new FoodCategory();
        created.populate(entity);

        when(foodCategoryService.create(any(FoodCategory.class))).thenReturn(10);
        when(foodCategoryService.get(10)).thenReturn(created);

        mockMvc.perform(post("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(header().string("Location", "http://localhost/food-category/categories/10"))
                .andExpect(jsonPath("$.name").value("Dairy"))
                .andExpect(jsonPath("$.description").value("Milk products"))
                .andExpect(jsonPath("$.group_id").value(10))
                .andExpect(jsonPath("$.created_by").value("Johnny Moo"))
                .andExpect(jsonPath("$.created_on").value("2025-10-01T10:20:43.000+08:00"))
                .andExpect(jsonPath("$.is_active").value(true));
    }

    @Test
    public void updateFoodCategory_shouldReturnNoContent() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setId(10);
        input.setName("Dairy");
        input.setDescription("Milk products derived from animals");
        input.setGroupId(100);
        input.setUpdatedBy("Johnny Moo");
        input.setIsActive(true);

        doNothing().when(foodCategoryService).update(any(FoodCategory.class));

        mockMvc.perform(put("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/food-category/categories/10"));
    }

    @Test
    public void patchFoodCategory_shouldReturnNoContent() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setId(11);
        input.setUpdatedBy("Jenny Bee");
        input.setName("Meat");

        doNothing().when(foodCategoryService).selectiveUpdate(any(FoodCategory.class));

        mockMvc.perform(patch("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/food-category/categories/11"));
    }

    @Test
    public void deleteFoodCategory_shouldReturnNoContent() throws Exception {
        doNothing().when(foodCategoryService).delete(1);

        mockMvc.perform(delete("/food-category/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateFoodCategoryWithLackingMandatoryFields_shouldReturnBadRequest() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setId(11);
        input.setDescription("Products derived from animals");
        input.setGroupId(5);
        input.setIsActive(false);
        // Missing name, updatedBy fields

        mockMvc.perform(put("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    JsonNode node = new ObjectMapper().readTree(content);
                    Assertions.assertTrue(node.get("message").asText().contains("{name}"));
                    Assertions.assertTrue(node.get("message").asText().contains("{updated_by}"));
                });
    }

    @Test
    public void patchFoodCategoryWithLackingMandatoryField_shouldReturnBadRequest() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setUpdatedBy("Jenny Bee");
        input.setIsActive(true);
        // Missing ID field

        mockMvc.perform(patch("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Provide the {id} of the category to be updated.]"));
    }

    @Test
    public void onResourceNotFoundException_shouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Id does not exist"))
                .when(foodCategoryService).get(999);

        mockMvc.perform(get("/food-category/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id does not exist"));
    }

    @Test
    public void onIllegalArgumentException_shouldReturnBadRequest() throws Exception {
        FoodCategory input = new FoodCategory();
        input.setId(11);
        input.setUpdatedBy("Jenny Bee");
        // Missing non-mandatory fields to update

        doThrow(new IllegalArgumentException("Zero non-mandatory fields found! Nothing to update."))
                .when(foodCategoryService).selectiveUpdate(any(FoodCategory.class));

        mockMvc.perform(patch("/food-category/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Zero non-mandatory fields found! Nothing to update."));
    }
}
