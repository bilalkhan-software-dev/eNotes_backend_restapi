package com.enotes.service;


import com.enotes.Dto.CategoryDto;
import com.enotes.Entity.Category;
import com.enotes.Exception.ExistDataException;
import com.enotes.Repository.CategoryRepository;
import com.enotes.Service.Impl.CategoryServiceImpl;
import com.enotes.Util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {


    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private Validation validation;

    @Mock
    private ModelMapper mapper;

    private CategoryDto categoryDto = null;
    private Category category = null;
    private List<CategoryDto> categoryDtoList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    @BeforeEach
    public void initializer(){

        categoryDto = CategoryDto.builder()
                .id(null)
                .name("My notes")
                .description("Personal notes")
                .isActive(true)
                .build();
        category = Category.builder()
                .id(null)
                .name("My notes")
                .description("Personal notes")
                .isActive(true)
                .isDeleted(false)
                .build();

        categoryDtoList.add(categoryDto);
        categoryList.add(category);
    }

    @Test
//    @Disabled
    public void testSaveCategory(){

        // Arrange
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
        when(mapper.map(categoryDto,Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);


        // Act
        boolean isCategorySaved = categoryService.addCategory(categoryDto);

        // Assert
        assertTrue(isCategorySaved);

        // Verify
        verify(validation).CategoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository).save(category);

    }


    @Test
//    @Disabled
    void testIsCategoryExist(){
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(true);
        ExistDataException exception = assertThrows(ExistDataException.class, () -> categoryService.addCategory(categoryDto));
        assertEquals("Category already exist with name :"+ categoryDto.getName(),exception.getMessage());
        verify(validation).CategoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository,never()).save(category);
    }

    @Test
    void testGetAllCategory(){
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(categoryList);
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        assertEquals(allCategory.size(),categoryList.size());
        verify(categoryRepository).findByIsDeletedFalse();
    }


}

