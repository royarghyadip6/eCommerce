package com.eCommerce.Ecom.Service.Impl;

import com.eCommerce.Ecom.Model.Category;
import com.eCommerce.Ecom.Repository.CategoryRepository;
import com.eCommerce.Ecom.Service.I_CategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements I_CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Read all the categories
     *
     * @return all category list
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Create a new category
     *
     * @param category to be created
     */
    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    /**
     * Update category
     *
     * @param category object to be updated
     */
    @Override
    public void updateCategory(Category category) {
        Optional<Category> categoryList = categoryRepository.findById(category.getCategoryId());
        logger.debug("Category List fetched from db: {}",categoryList);
        Category category1 = categoryList
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found"));
        logger.debug("Category : {}", category1);
        category1.setCategoryId(category.getCategoryId());
        category1.setCategoryName(category.getCategoryName());

        categoryRepository.save(category1);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the ID of the category to be removed
     * @return a success message containing the deleted ID
     */
    @Override
    public String deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return "Deleted id: " + categoryId + " successfully.";
    }
}
