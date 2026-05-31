package com.eCommerce.Ecom.Service.Impl;

import com.eCommerce.Ecom.DTO.CategoryDTO;
import com.eCommerce.Ecom.DTO.CategoryResponse;
import com.eCommerce.Ecom.Exception.APIException;
import com.eCommerce.Ecom.Exception.ResourceNotFoundException;
import com.eCommerce.Ecom.Model.Category;
import com.eCommerce.Ecom.Repository.CategoryRepository;
import com.eCommerce.Ecom.Service.I_CategoryService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements I_CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Read all the categories
     *
     * @return all category list
     */
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categoryList = categoryPage.getContent();
        if (categoryList.isEmpty()) {
            throw new APIException("Category is not present. Please create a new category.");
        } else {
            List<CategoryDTO> categoryDTOList = categoryList.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();

            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOList);
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalElements(categoryPage.getNumberOfElements());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setLastPage(categoryPage.isLast());

            return categoryResponse;
        }
    }

    /**
     * Create a new category
     *
     * @param categoryDTO to be created
     */
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new APIException("Category with name "+ categoryFromDb.getCategoryName() +" is already exists.");
        }
        System.out.println("Category not found. Creating category :"+ category.getCategoryName());
        Category cat = categoryRepository.save(category);
        CategoryDTO categoryDTO = modelMapper.map(cat, CategoryDTO.class);
        return categoryDTO;
    }

    /**
     * Update category
     *
     * @param category object to be updated
     */
    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Optional<Category> categoryList = categoryRepository.findById(category.getCategoryId());
        logger.debug("Category List fetched from db: {}",categoryList);
        Category category1 = categoryList
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",category.getCategoryId()));
        logger.debug("Category : {}", category1);
        category1.setCategoryId(category.getCategoryId());
        category1.setCategoryName(category.getCategoryName());

        category1 = categoryRepository.save(category1);

        CategoryDTO categoryDTO = modelMapper.map(category1, CategoryDTO.class);
        return categoryDTO;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId the ID of the category to be removed
     * @return a success message containing the deleted ID
     */
    @Override
    public CategoryDTO deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.deleteById(category.getCategoryId());
        return modelMapper.map(category, CategoryDTO.class);
    }
}
