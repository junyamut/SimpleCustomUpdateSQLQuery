package xyz.joseyamut.updatequerybuilder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

@Slf4j
@RestController
@RequestMapping(value = "/food-category")
public class FoodCategoryController {

    @Autowired
    private FoodCategoryService foodCategoryService;

    @GetMapping(path = "/get/{id}", produces = {"application/json"})
    @ResponseBody
    public FoodCategory getFoodCategory(@PathVariable(name = "id") Integer id) {
        return foodCategoryService.get(id);
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
        foodCategoryService.update(foodCategory);
        return foodCategoryService.get(foodCategory.getId());
    }
}
