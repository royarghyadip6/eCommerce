package com.eCommerce.Ecom.Service;

import com.eCommerce.Ecom.Model.Category;

import java.util.List;

public interface I_CategoryService {

    List<Category> getAllCategories();

    void createCategory(Category category);

    void updateCategory(Category category);

    String deleteCategoryById(Long categoryId);
}
