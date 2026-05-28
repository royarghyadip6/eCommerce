package com.eCommerce.Ecom.Service.Impl;

import com.eCommerce.Ecom.Model.Category;
import com.eCommerce.Ecom.Service.I_CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements I_CategoryService {

    private final List<Category> categoryList = new ArrayList<>();

    long id = 1;

    /**
     * @return
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryList;
    }

    /**
     * @param category
     */
    @Override
    public void createCategory(Category category) {
        category.setCategoryId(id++);
        categoryList.add(category);
    }

    /**
     * @param category
     */
    @Override
    public void updateCategory(Category category) {
        categoryList.stream()
                .filter(ele -> ele.getCategoryId() == category.getCategoryId())
                .findFirst()
                .ifPresentOrElse(ele -> {
                    ele.setCategoryId(category.getCategoryId());
                    ele.setCategoryName(category.getCategoryName());
                }, () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found")
                );
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public String deleteCategoryById(Long categoryId) {
        Category category = categoryList.stream()
                .filter(cat -> cat.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId+" not found"));

        categoryList.remove(category.getCategoryId());
        return "Deleted id: " + categoryId + " successfully.";
    }
}
