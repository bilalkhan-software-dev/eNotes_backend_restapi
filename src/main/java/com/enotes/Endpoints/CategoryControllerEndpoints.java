package com.enotes.Endpoints;

import com.enotes.Dto.CategoryDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Util.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoints {


    @PostMapping("/add-category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @GetMapping("/category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> getAllCategory();

    @GetMapping("/active-category")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN_OR_USER)
    public ResponseEntity<?> getAllActiveCategory();


    @GetMapping("/category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> getCategoryDetailById(@PathVariable Integer id);

    @DeleteMapping("/delete-category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);

    @PutMapping("/update-category/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN)
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) throws ResourceNotFoundException;



}
