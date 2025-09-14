package xyz.joseyamut.updatequerybuilder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/food-category/categories")
public class FoodCategoryController {
// Todo: add Delete method
    @Autowired
    private FoodCategoryService foodCategoryService;

    @GetMapping(path = "/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<FoodCategory> getFoodCategoryById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(foodCategoryService.get(id));
    }

    @GetMapping(produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<FoodCategory>> getFoodCategories(@RequestParam(name = "by", defaultValue = "id") String orderBy,
                                                    @RequestParam(name = "p", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "s", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(foodCategoryService.getList(orderBy, page, size));
    }

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<FoodCategory> createFoodCategory(@RequestBody FoodCategory foodCategory) {
        Integer id = foodCategoryService.create(foodCategory);
        FoodCategory createdFoodCategory = foodCategoryService.get(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(createdFoodCategory);
    }

    @PutMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<FoodCategory> updateFoodCategory(@RequestBody FoodCategory foodCategory) {
        /*if (foodCategory.getId() == null || foodCategory.getId() <= 0) {
            throw new IllegalArgumentException("Error : 'id' field missing : A valid Id is required.");
        }
        if (!StringUtils.hasText(foodCategory.getUpdatedBy())) {
            throw new IllegalArgumentException("Error : 'updated_by' field is missing.");
        }*/
        foodCategoryService.update(foodCategory);
        return ResponseEntity.noContent().build();
    }
}
