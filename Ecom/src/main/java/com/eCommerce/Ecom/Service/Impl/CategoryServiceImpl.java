package com.eCommerce.Ecom.Service.Impl;

import com.eCommerce.Ecom.Exception.APIException;
import com.eCommerce.Ecom.Exception.ResourceNotFoundException;
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
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.isEmpty()) {
            throw new APIException("Category is not present. Please create a new category.");
        } else {
            return categoryList;
        }
    }

    /**
     * Create a new category
     *
     * @param category to be created
     */
    @Override
    public void createCategory(Category category) {
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb == null) {
            System.out.println("Category not found. Creating category :"+category.getCategoryName());
            categoryRepository.save(category);
        } else {
            throw new APIException("Category with name "+ categoryFromDb.getCategoryName() +" is already exists.");
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",category.getCategoryId()));
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
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.deleteById(category.getCategoryId());
        return "Deleted id: " + categoryId + " successfully.";
    }
}
