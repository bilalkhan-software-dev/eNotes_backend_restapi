package com.enotes.Controller;

import com.enotes.Dto.CategoryDto;
import com.enotes.Dto.CategoryResponse;
import com.enotes.Endpoints.CategoryControllerEndpoints;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.CategoryService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerEndpoints {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<?> saveCategory(CategoryDto categoryDto) {

        boolean b = categoryService.addCategory(categoryDto);
        if (b) {
            return CommonUtil.createBuildResponseMessage("Category Saved Successfully", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Category Saved Failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> allCategory = categoryService.getAllCategory();

        if (CollectionUtils.isEmpty(allCategory)) {
          return  ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllActiveCategory() {

        List<CategoryResponse> allCategory = categoryService.getActiveCategory();

        if (CollectionUtils.isEmpty(allCategory)) {
           return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getCategoryDetailById(Integer id) {

        try {
            CategoryDto categoryById = categoryService.getCategoryById(id);

            if (!ObjectUtils.isEmpty(categoryById)) {
                return CommonUtil.createBuildResponse(categoryById, HttpStatus.OK);
            }

        } catch (ResourceNotFoundException e) {
            return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CommonUtil.createErrorResponseMessage("Category not found with id :" + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> deleteCategoryById(Integer id) {

        try {
            boolean isDeleted = categoryService.deleteCategory(id);
            if (isDeleted) {
                return CommonUtil.createBuildResponseMessage("Category Delete Successfully with id : " + id, HttpStatus.OK);
            }

        } catch (Exception e) {
            return CommonUtil.createErrorResponseMessage("Error occurs : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CommonUtil.createErrorResponseMessage("Category not delete with id :" + id, HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<?> updateCategory(Integer id, CategoryDto categoryDto) throws ResourceNotFoundException {

        boolean b = categoryService.updateCategory(id, categoryDto);
        if (b) {
            return CommonUtil.createBuildResponseMessage("Category update Successfully", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Category update Failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
