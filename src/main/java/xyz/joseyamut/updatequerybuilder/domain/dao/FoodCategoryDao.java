package xyz.joseyamut.updatequerybuilder.domain.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.repository.FoodCategoryRepository;
import xyz.joseyamut.updatequerybuilder.repository.builders.FoodCategoryQueryBuilder;

@Slf4j
@Service
public class FoodCategoryDao {

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private FoodCategoryQueryBuilder foodCategoryQueryBuilder;

    public FoodCategoryEntity getEntity(Integer id) {
        if (!foodCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Id does not exist");
        }
        return foodCategoryRepository.getOne(id);
    }

    public Integer createEntity(FoodCategoryEntity entity) {
        log.info("Entity before save: {}", entity);
        return foodCategoryRepository.saveAndFlush(entity).getId();
    }

    public void updateEntity(FoodCategoryEntity entity) {
        log.debug("Entity before update: {}", entity);
        foodCategoryQueryBuilder.setUpdateQueryStatement(entity);
        String queryStatement = foodCategoryQueryBuilder.getUpdateQueryStatement();
        log.debug("Update Query: {}", queryStatement);
        foodCategoryRepository.customQueryUpdate(queryStatement);
    }
}
