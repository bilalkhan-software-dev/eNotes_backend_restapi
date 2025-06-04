package com.enotes.Util;

import com.enotes.Dto.CategoryDto;
import com.enotes.Dto.NotesDto;
import com.enotes.Dto.TodoDto;
import com.enotes.Dto.TodoDto.StatusDto;
import com.enotes.Dto.UserDto;
import com.enotes.Dto.UserDto.RoleDto;
import com.enotes.Entity.Role;
import com.enotes.Exception.ValidationException;
import com.enotes.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class Validation {

    private final RoleRepository roleRepository;

    public void CategoryValidation(CategoryDto categoryDto) {
        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("Category object/JSON cannot be null or empty");
        }

        // Validate name
        String name = categoryDto.getName();
        if (ObjectUtils.isEmpty(name)) {
            error.put("name", "Name field cannot be null or empty");
        } else if (name.length() < 3 || name.length() > 50) {
            error.put("name", "Name must be between 3 and 50 characters");
        }

        // Validate description
        String description = categoryDto.getDescription();
        if (ObjectUtils.isEmpty(description)) {
            error.put("description", "Description field cannot be null or empty");
        } else if (description.length() < 3 || description.length() > 100) {
            error.put("description", "Description must be between 3 and 100 characters");
        }

        // Validate isActive
        if (categoryDto.getIsActive() == null) {
            error.put("isActive", "isActive field cannot be null");
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    public void NotesValidation(NotesDto notesDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(notesDto)) {
            throw new IllegalArgumentException("Notes object/JSON cannot be null or empty");
        }

        // Validate title
        String title = notesDto.getTitle();
        if (ObjectUtils.isEmpty(title)) {
            error.put("title", "Title field cannot be null or empty");
        } else if (title.length() < 3 || title.length() > 1000) {
            error.put("title", "Title must be between 3 and 100 characters");
        }

        // Validate description
        String description = notesDto.getDescription();
        if (ObjectUtils.isEmpty(description)) {
            error.put("description", "Description field cannot be null or empty");
        } else if (description.length() < 3 || description.length() > 10000) {
            error.put("description", "Description must be between 3 and 10000 characters");
        }

        if (!ObjectUtils.isEmpty(error)) {
            throw new ValidationException(error);
        }
    }

    public void TodoValidation(TodoDto todoDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(todoDto)) {
            throw new IllegalArgumentException("Todo object/JSON cannot be empty or null!");
        }

        String title = todoDto.getTitle();

        if (ObjectUtils.isEmpty(title)) {
            error.put("title", "Title field cannot be null or empty");
        } else if (title.length() < 3 || title.length() > 1000) {
            error.put("title", "Title must be between 3 and 1000 characters");
        }

        StatusDto requestedStatus = todoDto.getStatus();

        boolean statusFound = false;

        for (TodoStatus st : TodoStatus.values()) {
            if (st.getId().equals(requestedStatus.getId())) {
                statusFound = true;
                break;
            }
        }

        if (!statusFound) {
            error.put("status", "Invalid Status!");
        }

        if (!ObjectUtils.isEmpty(error)) {
            throw new ValidationException(error);
        }

    }

    public void UserValidation(UserDto userDto) {
        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(userDto)) {
            throw new IllegalArgumentException("User object/JSON cannot be null or empty");
        }

        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        String mobileNo = userDto.getMobileNo();


        if (firstName.matches("^\\d.*")) {
            error.put("first name", "First name cannot start with a number");

        } else if (!StringUtils.hasText(firstName)) {
            error.put("first name", "First name cannot be null or empty");
        }

        if (!StringUtils.hasText(lastName)) {
            error.put("last name", "Last name cannot be null or empty");
        }


        if (!StringUtils.hasText(email)) {
            error.put("email", "Email cannot be null or empty");
        } else if (!email.matches(AppConstant.EMAIL_REGEX)) {
            error.put("email", "Invalid email format");
        }


        if (!StringUtils.hasText(password)) {
            error.put("password", "Password cannot be null or empty");
        } else if (password.length() < 6) {
            error.put("password", "Password must be at least 6 characters long");
        }

        if (!StringUtils.hasText(mobileNo)) {
            error.put("mobile number", "Mobile number cannot be null or empty");
        } else if (!mobileNo.matches(AppConstant.MOBILE_NO_REGEX)) {
            error.put("mobile number", "Invalid Pakistani mobile number format");
        }


        List<Integer> roleAvailableInDatabase =
                roleRepository.findAll()
                .stream()
                .map(Role::getId)
                .toList();

        List<Integer> requestRoleList =
                userDto.getRoles()
                .stream()
                .map(RoleDto::getId)
                .filter(roleAvailableInDatabase::contains)
                .toList();

        if (CollectionUtils.isEmpty(userDto.getRoles())){
            error.put("role","Role field cannot be null or empty!");
        } else if (CollectionUtils.isEmpty(requestRoleList)) {
            error.put("role","Invalid role id!");
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }


}
