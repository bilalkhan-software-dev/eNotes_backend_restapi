package com.enotes.Endpoints;

import com.enotes.Dto.CategoryDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Util.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category",description = "All the category operation apis")
@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoints {


    @Operation(summary = "Save category",tags = {"Category"},description = "Accessible only for admin")
    @PostMapping("/add-category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @Operation(summary = "All category",tags = {"Category"},description = "Accessible only for admin")
    @GetMapping("/category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> getAllCategory();

    @Operation(summary = "All active category",tags = {"Category"},description = "Accessible for both admin and user")
    @GetMapping("/active-category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN_OR_USER)
    public ResponseEntity<?> getAllActiveCategory();


    @Operation(summary = "Category details by id",tags = {"Category"},description = "Accessible only for admin")
    @GetMapping("/category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> getCategoryDetailById(@PathVariable Integer id);

    @Operation(summary = "Delete category by id",tags = {"Category"},description = "Accessible only for admin")
    @DeleteMapping("/delete-category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);

    @Operation(summary = "Update category by id",tags = {"Category"},description = "Accessible only for admin")
    @PutMapping("/update-category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) throws ResourceNotFoundException;

}
