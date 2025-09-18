package xyz.joseyamut.updatequerybuilder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.joseyamut.updatequerybuilder.domain.validation.CreateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.validation.UpdateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/food-category/categories")
@Validated
public class FoodCategoryController {

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
    @Validated(CreateFoodCategoryGroup.class)
    @ResponseBody
    public ResponseEntity<FoodCategory> createFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        Integer id = foodCategoryService.create(foodCategory);
        FoodCategory createdFoodCategory = foodCategoryService.get(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(createdFoodCategory);
    }

    @PutMapping(produces = {"application/json"}, consumes = {"application/json"})
    @Validated(UpdateFoodCategoryGroup.class)
    @ResponseBody
    public ResponseEntity<FoodCategory> updateFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        foodCategoryService.update(foodCategory);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFoodCategory(@PathVariable(name = "id") Integer id) {
        foodCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
