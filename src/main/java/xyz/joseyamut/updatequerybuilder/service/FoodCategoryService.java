package xyz.joseyamut.updatequerybuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.joseyamut.updatequerybuilder.domain.dao.FoodCategoryDao;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;

@Slf4j
@Service
public class FoodCategoryService {

    @Autowired
    private FoodCategoryDao foodCategoryDao;

    public FoodCategory get(Integer id) {
        FoodCategoryEntity foodCategoryEntity = foodCategoryDao.getEntity(id);
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.populate(foodCategoryEntity);
        return foodCategory;
    }

    public Integer create(FoodCategory dto) {
        FoodCategoryEntity foodCategoryEntity = new FoodCategoryEntity();
        foodCategoryEntity.populate(dto);
        log.info("Food Category: {}", foodCategoryEntity);
        return foodCategoryDao.createEntity(foodCategoryEntity);
    }

    public void update(FoodCategory dto) {
        FoodCategoryEntity foodCategoryEntity = new FoodCategoryEntity();
        foodCategoryEntity.update(dto);
        foodCategoryDao.updateEntity(foodCategoryEntity);
    }
}
