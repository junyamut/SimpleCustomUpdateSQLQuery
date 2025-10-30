package xyz.joseyamut.updatequerybuilder.domain.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.domain.exception.ResourceNotFoundException;
import xyz.joseyamut.updatequerybuilder.repository.FoodCategoryRepository;
import xyz.joseyamut.updatequerybuilder.repository.util.FoodCategoryQueryBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodCategoryRepositoryDaoTest {

    @Mock
    private FoodCategoryRepository foodCategoryRepository;

    @Mock
    private FoodCategoryQueryBuilder foodCategoryQueryBuilder;

    @InjectMocks
    private FoodCategoryDao foodCategoryDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEntityExists() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(foodCategoryRepository.existsById(1)).thenReturn(true);
        when(foodCategoryRepository.getReferenceById(1)).thenReturn(entity);

        FoodCategoryEntity result = foodCategoryDao.getEntity(1);

        assertEquals(entity, result);
        verify(foodCategoryRepository).getReferenceById(1);
    }

    @Test
    void testGetEntityNotExistsThrows() {
        when(foodCategoryRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> foodCategoryDao.getEntity(2));
    }

    @Test
    void testListEntitiesOrderById() {
        FoodCategoryEntity e1 = mock(FoodCategoryEntity.class);
        when(foodCategoryRepository.listByPageOrderById(any())).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(e1)));

        List<FoodCategoryEntity> result = foodCategoryDao.listEntities("id", 0, 10);

        assertEquals(1, result.size());
        verify(foodCategoryRepository).listByPageOrderById(any());
    }

    @Test
    void testListEntitiesOrderByName() {
        FoodCategoryEntity e1 = mock(FoodCategoryEntity.class);
        when(foodCategoryRepository.listByPageOrderByName(any())).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(e1)));

        List<FoodCategoryEntity> result = foodCategoryDao.listEntities("name", 0, 10);

        assertEquals(1, result.size());
        verify(foodCategoryRepository).listByPageOrderByName(any());
    }

    @Test
    void testCreateEntity() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(foodCategoryRepository.saveAndFlush(entity)).thenReturn(entity);
        when(entity.getId()).thenReturn(42);

        Integer id = foodCategoryDao.createEntity(entity);

        assertEquals(42, id);
        verify(foodCategoryRepository).saveAndFlush(entity);
    }

    @Test
    void testUpdateEntityExists() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(entity.getId()).thenReturn(1);
        when(foodCategoryRepository.existsById(1)).thenReturn(true);

        foodCategoryDao.updateEntity(entity);

        verify(foodCategoryRepository).saveAndFlush(entity);
    }

    @Test
    void testUpdateEntityNotExistsThrows() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(entity.getId()).thenReturn(2);
        when(foodCategoryRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> foodCategoryDao.updateEntity(entity));
    }

    @Test
    void testSelectiveUpdateEntityExists() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(entity.getId()).thenReturn(1);
        when(foodCategoryRepository.existsById(1)).thenReturn(true);
        when(foodCategoryQueryBuilder.getUpdateQueryStatement()).thenReturn("UPDATE ...");

        foodCategoryDao.selectiveUpdateEntity(entity);

        verify(foodCategoryQueryBuilder).setUpdateQueryStatement(entity);
        verify(foodCategoryRepository).update("UPDATE ...");
    }

    @Test
    void testSelectiveUpdateEntityNotExistsThrows() {
        FoodCategoryEntity entity = mock(FoodCategoryEntity.class);
        when(entity.getId()).thenReturn(2);
        when(foodCategoryRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> foodCategoryDao.selectiveUpdateEntity(entity));
    }

    @Test
    void testDeleteEntityExists() {
        when(foodCategoryRepository.existsById(1)).thenReturn(true);

        foodCategoryDao.deleteEntity(1);

        verify(foodCategoryRepository).deleteById(1);
    }

    @Test
    void testDeleteEntityNotExistsThrows() {
        when(foodCategoryRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> foodCategoryDao.deleteEntity(2));
    }
}