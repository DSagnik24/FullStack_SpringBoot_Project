package com.sagnik.Ecom.service;

import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.payload.CategoryDTO;
import com.sagnik.Ecom.payload.CategoryResponse;

import java.util.List;


public interface CategoryService {
     CategoryResponse getAllCategories();
     CategoryDTO createCategory(CategoryDTO categoryDto);
     String deleteCategory(Long categoryId);
     CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

}
