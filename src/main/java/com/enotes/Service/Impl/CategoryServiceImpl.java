package com.enotes.Service.Impl;

import com.enotes.Dto.CategoryDto;
import com.enotes.Dto.CategoryResponse;
import com.enotes.Entity.Category;
import com.enotes.Exception.ExistDataException;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Repository.CategoryRepository;
import com.enotes.Service.CacheManagerService;
import com.enotes.Service.CategoryService;
import com.enotes.Util.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final Validation validation;
    private final CacheManagerService cacheManagerService;


    @Override
    public boolean addCategory(CategoryDto categoryDto) {

        // chacking validation before processing
        validation.CategoryValidation(categoryDto);

        // checking existing category name
        boolean isExisted = categoryRepo.existsByName(categoryDto.getName().trim());

        if (isExisted) {
            log.error("categoryServiceImpl Category already exist trim : {}", categoryDto.getName().trim());
            log.error("categoryServiceImpl Category already exist  : {}", categoryDto.getName());
            throw new ExistDataException("Category already exist with name :" + categoryDto.getName().trim());
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        category.setIsDeleted(false);
        Category save = categoryRepo.save(category);
        return !ObjectUtils.isEmpty(save);
    }


    @Override
    @Cacheable("allCategory")
    public List<CategoryDto> getAllCategory() {

        List<Category> categories = categoryRepo.findByIsDeletedFalse();

        return categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();

    }

    @Override
    @Cacheable("activeCategory")
    public List<CategoryResponse> getActiveCategory() {

        List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();

        return categories.stream().map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
    }


    @Override
    @Cacheable(value = "categoryById", key = "#id")
    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {

        Category categoryById = categoryRepo.findByIdAndIsDeletedFalse(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id : " + id));

        return modelMapper.map(categoryById, CategoryDto.class);
    }

    @Override
    @CacheEvict(value = "categoryById", key = "#id")
    public boolean deleteCategory(Integer id) {

        Optional<Category> category = categoryRepo.findByIdAndIsDeletedFalse(id);

        if (category.isPresent()) {
            Category category1 = category.get();
            category1.setIsDeleted(true);
            categoryRepo.save(category1);

            // after delete, we also needed it to delete this cache
            cacheManagerService.removeCacheByName(Arrays.asList("allCategory", "activeCategory"));

            return true;
        }

        return false;
    }

    @Override
    public boolean updateCategory(Integer id, CategoryDto categoryDto) throws ResourceNotFoundException {

        validation.CategoryValidation(categoryDto);

        return categoryRepo.findByIdAndIsDeletedFalse(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDto.getName());
                    existingCategory.setDescription(categoryDto.getDescription());

                    return !ObjectUtils.isEmpty(categoryRepo.save(existingCategory));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));
    }
}
