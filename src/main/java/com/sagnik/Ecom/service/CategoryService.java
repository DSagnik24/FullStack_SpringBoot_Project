package com.sagnik.Ecom.service;

import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.payload.CategoryDTO;
import com.sagnik.Ecom.payload.CategoryResponse;

import java.util.List;


public interface CategoryService {
     CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy, String order);
     CategoryDTO createCategory(CategoryDTO categoryDto);
     CategoryDTO deleteCategory(Long categoryId);
     CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

}
