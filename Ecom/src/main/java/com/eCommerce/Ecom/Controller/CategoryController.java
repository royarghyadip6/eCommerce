package com.eCommerce.Ecom.Controller;

import com.eCommerce.Ecom.DTO.CategoryDTO;
import com.eCommerce.Ecom.DTO.CategoryResponse;
import com.eCommerce.Ecom.Model.Category;
import com.eCommerce.Ecom.Service.I_CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final I_CategoryService categoryService;

    public CategoryController(I_CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse category = categoryService.getAllCategories();
        return ResponseEntity.ok(category);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDto) {
        CategoryDTO categoryDTO = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @PutMapping("/public/categories")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDto) {
        CategoryDTO categoryDTO = categoryService.updateCategory(categoryDto);
        return new ResponseEntity<>(categoryDTO,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }
}
