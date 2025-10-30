package xyz.joseyamut.updatequerybuilder.domain.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.domain.exception.ResourceNotFoundException;
import xyz.joseyamut.updatequerybuilder.repository.FoodCategoryRepository;
import xyz.joseyamut.updatequerybuilder.repository.util.FoodCategoryQueryBuilder;

import java.util.List;

@Slf4j
@Component
public class FoodCategoryDao {

    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodCategoryQueryBuilder foodCategoryQueryBuilder;

    public FoodCategoryDao(FoodCategoryRepository foodCategoryRepository, FoodCategoryQueryBuilder foodCategoryQueryBuilder) {
        this.foodCategoryRepository = foodCategoryRepository;
        this.foodCategoryQueryBuilder = foodCategoryQueryBuilder;
    }

    public FoodCategoryEntity getEntity(Integer id) {
        if (!foodCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id does not exist");
        }
        return foodCategoryRepository.getReferenceById(id);
    }

    public List<FoodCategoryEntity> listEntities(String orderBy, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        // default to ordering by Id
        Page<FoodCategoryEntity> foodCategoryEntityPage = foodCategoryRepository.listByPageOrderById(pageable);
        if (orderBy.equalsIgnoreCase("name")) {
            foodCategoryEntityPage = foodCategoryRepository.listByPageOrderByName(pageable);
        }
        return foodCategoryEntityPage.getContent();
    }

    public Integer createEntity(FoodCategoryEntity entity) {
        log.debug("Entity before save: {}", entity);
        return foodCategoryRepository.saveAndFlush(entity).getId();
    }

    public void updateEntity(FoodCategoryEntity entity) {
        if (!foodCategoryRepository.existsById(entity.getId())) {
            throw new ResourceNotFoundException("Id does not exist");
        }
        log.debug("Entity for update: {}", entity);
        foodCategoryRepository.saveAndFlush(entity);
    }

    public void selectiveUpdateEntity(FoodCategoryEntity entity) {
        if (!foodCategoryRepository.existsById(entity.getId())) {
            throw new ResourceNotFoundException("Id does not exist");
        }
        log.debug("Entity for update: {}", entity);
        foodCategoryQueryBuilder.setUpdateQueryStatement(entity);
        String queryStatement = foodCategoryQueryBuilder.getUpdateQueryStatement();
        log.debug("Update Query: {}", queryStatement);
        foodCategoryRepository.update(queryStatement);
    }

    public void deleteEntity(Integer id) {
        if (!foodCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id does not exist");
        }
        foodCategoryRepository.deleteById(id);
        log.warn("Entity with Id {} deleted!", id);
    }
}
