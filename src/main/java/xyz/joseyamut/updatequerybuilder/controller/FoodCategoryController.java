package xyz.joseyamut.updatequerybuilder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/food-category")
public class FoodCategoryController {

    @Autowired
    private FoodCategoryService foodCategoryService;

    @GetMapping(path = "/categories/{id}", produces = {"application/json"})
    @ResponseBody
    public FoodCategory getFoodCategoryById(@PathVariable(name = "id") Integer id) {
        return foodCategoryService.get(id);
    }

    @GetMapping(path = "/categories", produces = {"application/json"})
    @ResponseBody
    public List<FoodCategory> getFoodCategories(@RequestParam(name = "by", defaultValue = "id") String orderBy,
                                                    @RequestParam(name = "p", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "s", defaultValue = "5") Integer size) {
        return foodCategoryService.getList(orderBy, page, size);
    }

    @PostMapping(value = "/create", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public FoodCategory createFoodCategory(@RequestBody FoodCategory foodCategory) {
        Integer id = foodCategoryService.create(foodCategory);
        return foodCategoryService.get(id);
    }

    @PutMapping(value = "/update", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public FoodCategory updateFoodCategory(@RequestBody FoodCategory foodCategory) {
        if (foodCategory.getId() == null || foodCategory.getId() <= 0) {
            throw new IllegalArgumentException("Error : 'id' field missing : A valid Id is required.");
        }
        if (!StringUtils.hasText(foodCategory.getUpdatedBy())) {
            throw new IllegalArgumentException("Error : 'updated_by' field is missing.");
        }
        foodCategoryService.update(foodCategory);
        return foodCategoryService.get(foodCategory.getId());
    }
}
