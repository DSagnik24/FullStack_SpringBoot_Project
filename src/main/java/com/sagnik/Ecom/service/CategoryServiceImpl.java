package com.sagnik.Ecom.service;

import com.sagnik.Ecom.Exceptions.APIException;
import com.sagnik.Ecom.Exceptions.ResourceNotFoundException;
import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.payload.CategoryDTO;
import com.sagnik.Ecom.payload.CategoryResponse;
import com.sagnik.Ecom.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    /** Builds a paginated response containing all available categories. */
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy, String order) {
        Sort sortByAndOrder = order.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No Category created");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    /** Validates the category name, saves the category, and returns its DTO. */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategoryFromDB != null){
            throw new APIException("Category with the name "+category.getCategoryName()+" already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    /** Removes the requested category and returns the deleted data. */
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException(categoryId,"Category","categoryId"));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    /** Replaces the requested category data and returns the saved DTO. */
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId){
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException(categoryId,"Category","categoryId"));

        category.setCategoryId(categoryId);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
