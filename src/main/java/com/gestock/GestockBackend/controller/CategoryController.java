package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.persistence.entity.CategoryEntity;
import com.gestock.GestockBackend.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity category) {
        CategoryEntity savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable Long id) {
        Optional<CategoryEntity> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getAllCategories() {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(@PathVariable Long id, @RequestBody CategoryEntity categoryDetails) {
        Optional<CategoryEntity> existingCategoryOpt = categoryService.getCategoryById(id);
        if (existingCategoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CategoryEntity existingCategory = existingCategoryOpt.get();
        existingCategory.setName(categoryDetails.getName());

        CategoryEntity updatedCategory = categoryService.saveCategory(existingCategory);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Optional<CategoryEntity> existingCategory = categoryService.getCategoryById(id);
        if (existingCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

