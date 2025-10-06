package xyz.joseyamut.updatequerybuilder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.joseyamut.updatequerybuilder.domain.validation.PostFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.validation.PatchFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;
import xyz.joseyamut.updatequerybuilder.domain.validation.PutFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.service.FoodCategoryService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/food-category/categories")
@Validated
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    public FoodCategoryController(FoodCategoryService foodCategoryService) {
        this.foodCategoryService = foodCategoryService;
    }

    @GetMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<FoodCategory> getFoodCategoryById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(foodCategoryService.get(id));
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<FoodCategory>> getFoodCategories(@RequestParam(name = "by", defaultValue = "id") String orderBy,
                                                    @RequestParam(name = "p", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "s", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(foodCategoryService.getList(orderBy, page, size));
    }

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @Validated(PostFoodCategoryGroup.class)
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

    @PutMapping(consumes = {"application/json"})
    @Validated(PutFoodCategoryGroup.class)
    public ResponseEntity<Void> updateFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        foodCategoryService.update(foodCategory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategory.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", String.valueOf(location))
                .build();
    }

    @PatchMapping(consumes = {"application/json"})
    @Validated(PatchFoodCategoryGroup.class)
    public ResponseEntity<Void> patchFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        foodCategoryService.selectiveUpdate(foodCategory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategory.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", String.valueOf(location))
                .build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteFoodCategory(@PathVariable(name = "id") Integer id) {
        foodCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
