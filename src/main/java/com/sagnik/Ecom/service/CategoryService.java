package com.sagnik.Ecom.service;

import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.payload.CategoryDTO;
import com.sagnik.Ecom.payload.CategoryResponse;

import java.util.List;


public interface CategoryService {
     /** Retrieves categories in a paginated and sorted response. */
     CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy, String order);
     /** Creates a category from the supplied DTO. */
     CategoryDTO createCategory(CategoryDTO categoryDto);
     /** Deletes a category and returns the deleted category as a DTO. */
     CategoryDTO deleteCategory(Long categoryId);
     /** Updates a category and returns the saved category as a DTO. */
     CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

}
