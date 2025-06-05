package com.enotes.Controller;

import com.enotes.Dto.LoginRequest;
import com.enotes.Dto.LoginResponse;
import com.enotes.Dto.UserDto;
import com.enotes.Service.AuthService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {

        boolean isRegistered = authService.registerUser(userDto);
        if (isRegistered) {
            return CommonUtil.createBuildResponseMessage("Registration Successfully! Email with verification link has sent to your mail address", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Registration Failed! Try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        LoginResponse response = authService.authenticateLoginUser(loginRequest);
        if (!ObjectUtils.isEmpty(response)){
            return CommonUtil.createBuildResponse(response,HttpStatus.OK);
        }

        return CommonUtil.createErrorResponse("Invalid Credential",HttpStatus.BAD_REQUEST);
    }


}
