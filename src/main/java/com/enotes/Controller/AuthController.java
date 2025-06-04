package com.enotes.Controller;

import com.enotes.Dto.UserDto;
import com.enotes.Service.AuthService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {

        boolean isRegistered = userService.registerUser(userDto);
        if (isRegistered) {
            return CommonUtil.createBuildResponseMessage("Registration Successfully! Email with verification link has sent to your mail address", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Registration Failed! Try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
