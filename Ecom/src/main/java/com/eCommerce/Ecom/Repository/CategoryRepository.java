package com.eCommerce.Ecom.Repository;

import com.eCommerce.Ecom.Model.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryName(@NotEmpty @Size(min = 5, message = "Category Name Must Be At Least 5") String categoryName);
}
