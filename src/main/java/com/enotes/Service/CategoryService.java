package com.enotes.Service;

import com.enotes.Dto.CategoryDto;
import com.enotes.Dto.CategoryResponse;
import com.enotes.Exception.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {

    boolean addCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategory();

    List<CategoryResponse> getActiveCategory();

    CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;

    boolean deleteCategory(Integer id) ;

    boolean updateCategory(Integer id,CategoryDto categoryDto) throws ResourceNotFoundException;


}
