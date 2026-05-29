package com.eCommerce.Ecom.Repository;

import com.eCommerce.Ecom.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
