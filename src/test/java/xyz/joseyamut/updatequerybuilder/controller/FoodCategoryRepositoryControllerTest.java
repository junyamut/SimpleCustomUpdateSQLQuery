package xyz.joseyamut.updatequerybuilder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.domain.exception.ResourceNotFoundException;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodCategoryRepositoryControllerTest {

    @Mock
    private FoodCategoryService foodCategoryService;

    @InjectMocks
    private FoodCategoryController foodCategoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String basePath = "/food-category/categories";

    private FoodCategory foodCategory(Integer id) {
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setId(id);
        foodCategory.setGroupId(id);
        foodCategory.setName("Name");
        foodCategory.setDescription("Description");
        foodCategory.setCreatedBy("Tester");
        foodCategory.setCreatedOn("2025-10-13T20:19:45.650+08:00");
        foodCategory.setIsActive(true);
        return foodCategory;
    }

    @Test
    public void getFoodCategoryById_whenIdExists() {
        int mockId = 1;
        FoodCategory mockCategory = foodCategory(mockId);
        when(foodCategoryService.get(mockId)).thenReturn(mockCategory);

        ResponseEntity<FoodCategory> response = foodCategoryController.getFoodCategoryById(mockId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCategory, response.getBody());
    }

    @Test
    public void getFoodCategoryById_whenIdDoesNotExist_throwsException() {
        int mockId = 99;
        when(foodCategoryService.get(mockId)).thenThrow(new ResourceNotFoundException("Id does not exist"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> foodCategoryController.getFoodCategoryById(mockId));

        assertEquals("Id does not exist", exception.getMessage());
    }

    @Test
    public void getFoodCategories_returnList() {
        List<FoodCategory> mockCategories = List.of(new FoodCategory(), new FoodCategory());
        when(foodCategoryService.getList("id", 0, 5)).thenReturn(mockCategories);

        ResponseEntity<List<FoodCategory>> response = foodCategoryController.getFoodCategories("id", 0, 5);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCategories, response.getBody());
    }

    @ParameterizedTest
    @ValueSource(ints = 123)
    public void createFoodCategory(Integer mockId) {
        String expectedUri = basePath + "/" + mockId;

        FoodCategory mockCategory = foodCategory(mockId);
        when(foodCategoryService.create(any(FoodCategory.class))).thenReturn(mockId);
        when(foodCategoryService.get(mockId)).thenReturn(mockCategory);

        try(MockedStatic<ServletUriComponentsBuilder> mockedStaticBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder mockBuilder = mock(ServletUriComponentsBuilder.class);
            mockedStaticBuilder.when(ServletUriComponentsBuilder::fromCurrentRequest).thenReturn(mockBuilder);
            when(mockBuilder.path("/{id}")).thenReturn(mockBuilder);

            UriComponents uriComponents = mock(UriComponents.class);
            when(mockBuilder.buildAndExpand(mockId)).thenReturn(uriComponents);
            URI uri = URI.create(expectedUri);
            when(uriComponents.toUri()).thenReturn(uri);

            ResponseEntity<FoodCategory> response = foodCategoryController.createFoodCategory(mockCategory);

            assertEquals(201, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(mockCategory.getId(), response.getBody().getId());
            assertNull(response.getBody().getUpdatedBy());
            assertNull(response.getBody().getUpdatedOn());
            assertEquals(uri, response.getHeaders().getLocation());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = 123)
    void updateFoodCategory(Integer mockId) {
        String expectedUri = basePath + "/" + mockId;

        FoodCategory mockCategory = foodCategory(mockId);
        mockCategory.setUpdatedBy("Updater");
        mockCategory.setUpdatedOn("2025-10-13T21:01:29.010+08:00");
        doNothing().when(foodCategoryService).update(any(FoodCategory.class));
        when(foodCategoryService.get(mockId)).thenReturn(mockCategory);

        try(MockedStatic<ServletUriComponentsBuilder> mockedStaticBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder mockBuilder = mock(ServletUriComponentsBuilder.class);
            mockedStaticBuilder.when(ServletUriComponentsBuilder::fromCurrentRequest).thenReturn(mockBuilder);
            when(mockBuilder.path("/{id}")).thenReturn(mockBuilder);

            UriComponents uriComponents = mock(UriComponents.class);
            when(mockBuilder.buildAndExpand(mockId)).thenReturn(uriComponents);
            URI uri = URI.create(expectedUri);
            when(uriComponents.toUri()).thenReturn(uri);

            ResponseEntity<Void> response = foodCategoryController.updateFoodCategory(mockCategory);

            verify(foodCategoryService, times(1)).update(mockCategory);
            assertEquals(204, response.getStatusCode().value());
            assertNull(response.getBody());
            assertEquals(uri, response.getHeaders().getLocation());
        }
    }

    @Test
    public void updateFoodCategory_whenIdDoesNotExist_throwsException() {
        FoodCategory mockCategory = foodCategory(99);
        doThrow(new ResourceNotFoundException("Id does not exist")).when(foodCategoryService).update(mockCategory);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> foodCategoryController.updateFoodCategory(mockCategory));

        assertEquals("Id does not exist", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = 123)
    void patchFoodCategory(Integer mockId) {
        String expectedUri = basePath + "/" + mockId;

        FoodCategory mockCategory = new FoodCategory();
        mockCategory.setId(mockId);
        mockCategory.setUpdatedBy("Updater Tester");
        mockCategory.setDescription("I have updated the description.");
        doNothing().when(foodCategoryService).selectiveUpdate(any(FoodCategory.class));
        when(foodCategoryService.get(mockId)).thenReturn(mockCategory);

        try(MockedStatic<ServletUriComponentsBuilder> mockedStaticBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder mockBuilder = mock(ServletUriComponentsBuilder.class);
            mockedStaticBuilder.when(ServletUriComponentsBuilder::fromCurrentRequest).thenReturn(mockBuilder);
            when(mockBuilder.path("/{id}")).thenReturn(mockBuilder);

            UriComponents uriComponents = mock(UriComponents.class);
            when(mockBuilder.buildAndExpand(mockId)).thenReturn(uriComponents);
            URI uri = URI.create(expectedUri);
            when(uriComponents.toUri()).thenReturn(uri);

            ResponseEntity<Void> response = foodCategoryController.patchFoodCategory(mockCategory);

            verify(foodCategoryService, times(1)).selectiveUpdate(mockCategory);
            assertEquals(204, response.getStatusCode().value());
            assertNull(response.getBody());
            assertEquals(uri, response.getHeaders().getLocation());
        }
    }

    @Test
    void deleteFoodCategory() {
        int mockId = 2;
        ResponseEntity<Void> response = foodCategoryController.deleteFoodCategory(mockId);

        verify(foodCategoryService, times(1)).delete(mockId);
        assertEquals(204, response.getStatusCode().value());
    }
}