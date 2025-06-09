package com.enotes.Endpoints;

import com.enotes.Dto.LoginRequest;
import com.enotes.Dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication",description = "Authenticate user apis")
@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoints {

    @Operation(summary = "User registration endpoint",tags = {"Authentication","Home"})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception;

    @Operation(summary = "User login endpoint",tags = {"Authentication","Home"})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);

}
