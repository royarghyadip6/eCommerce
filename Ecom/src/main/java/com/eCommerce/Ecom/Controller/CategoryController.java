package com.eCommerce.Ecom.Controller;

import com.eCommerce.Ecom.Model.Category;
import com.eCommerce.Ecom.Service.I_CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final I_CategoryService categoryService;

    public CategoryController(I_CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created");
    }

    @PutMapping("/public/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        try {
            System.out.println("Update got hit");
            categoryService.updateCategory(category);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategoryById(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }
}
