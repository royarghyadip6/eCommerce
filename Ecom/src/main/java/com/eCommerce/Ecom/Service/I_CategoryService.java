package com.eCommerce.Ecom.Service;

import com.eCommerce.Ecom.DTO.CategoryDTO;
import com.eCommerce.Ecom.DTO.CategoryResponse;
import com.eCommerce.Ecom.Model.Category;

public interface I_CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDto);

    CategoryDTO deleteCategoryById(Long categoryId);
}
