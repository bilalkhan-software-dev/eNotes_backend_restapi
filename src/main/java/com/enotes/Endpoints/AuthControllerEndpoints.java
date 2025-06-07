package com.enotes.Endpoints;

import com.enotes.Dto.LoginRequest;
import com.enotes.Dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoints {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);

}
