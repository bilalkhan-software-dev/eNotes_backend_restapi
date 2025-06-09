package com.enotes.Endpoints;

import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User",description = "Accessible if user logged in")
@RequestMapping("/api/v1/user")
public interface UserControllerEndpoints {

    @Operation(summary = "User profile ",tags = {"User"})
    @GetMapping("/profile")
    public ResponseEntity<?> profile();

    @Operation(summary = "User password changed",tags = {"User"})
    @PutMapping("password-changed")
    public ResponseEntity<?> changedPassword(@RequestBody PasswordChangedRequest passwordChangedRequest) throws ResourceNotFoundException;


}
