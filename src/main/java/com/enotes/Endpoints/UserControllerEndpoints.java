package com.enotes.Endpoints;

import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/user")
public interface UserControllerEndpoints {

    @GetMapping("/profile")
    public ResponseEntity<?> profile();

    @PutMapping("password-changed")
    public ResponseEntity<?> changedPassword(@RequestBody PasswordChangedRequest passwordChangedRequest) throws ResourceNotFoundException;


}
