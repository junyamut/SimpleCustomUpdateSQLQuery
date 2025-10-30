package xyz.joseyamut.updatequerybuilder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.joseyamut.updatequerybuilder.domain.dao.FoodCategoryDao;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodCategoryRepositoryServiceTest {

    @Mock
    private FoodCategoryDao foodCategoryDao;

    @InjectMocks
    private FoodCategoryService foodCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGet() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(foodCategoryDao.getEntity(1)).thenReturn(entity);

        FoodCategory result = foodCategoryService.get(1);

        assertNotNull(result);
        verify(foodCategoryDao).getEntity(1);
    }

    @Test
    void testGetList() {
        FoodCategoryEntity entity1 = mock(FoodCategoryEntity.class);
        FoodCategoryEntity entity2 = mock(FoodCategoryEntity.class);
        when(foodCategoryDao.listEntities("name", 0, 10)).thenReturn(Arrays.asList(entity1, entity2));

        List<FoodCategory> result = foodCategoryService.getList("name", 0, 10);

        assertEquals(2, result.size());
        verify(foodCategoryDao).listEntities("name", 0, 10);
    }

    @Test
    void testCreate() {
        FoodCategory dto = mock(FoodCategory.class);
        ArgumentCaptor<FoodCategoryEntity> captor = ArgumentCaptor.forClass(FoodCategoryEntity.class);
        when(foodCategoryDao.createEntity(any())).thenReturn(42);

        Integer id = foodCategoryService.create(dto);

        assertEquals(42, id);
        verify(foodCategoryDao).createEntity(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    void testUpdate() {
        FoodCategory dto = mock(FoodCategory.class);
        ArgumentCaptor<FoodCategoryEntity> captor = ArgumentCaptor.forClass(FoodCategoryEntity.class);

        foodCategoryService.update(dto);

        verify(foodCategoryDao).updateEntity(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    void testSelectiveUpdate() {
        FoodCategory dto = mock(FoodCategory.class);
        ArgumentCaptor<FoodCategoryEntity> captor = ArgumentCaptor.forClass(FoodCategoryEntity.class);

        foodCategoryService.selectiveUpdate(dto);

        verify(foodCategoryDao).selectiveUpdateEntity(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    void testDelete() {
        foodCategoryService.delete(5);
        verify(foodCategoryDao).deleteEntity(5);
    }
}