package com.sagnik.Ecom.repository;

import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    /** Finds products in a category ordered from lowest to highest price. */
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    /** Finds products whose names contain the keyword, ignoring case. */
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
