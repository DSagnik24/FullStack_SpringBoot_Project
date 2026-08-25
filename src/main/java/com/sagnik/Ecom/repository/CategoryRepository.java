package com.sagnik.Ecom.repository;

import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.payload.CategoryDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    /** Finds a category by its unique name. */
    Category findByCategoryName(@NotBlank String categoryName);

}
