package com.enotes.Controller;


import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.UserResponse;
import com.enotes.Entity.User;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.UserService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<?> profile()  {
        User user = CommonUtil.GetLoggedInUserDetails();
        UserResponse userDetails = mapper.map(user, UserResponse.class);
        return CommonUtil.createBuildResponse(userDetails, HttpStatus.OK);
    }



    @PutMapping("password-changed")
    public ResponseEntity<?> changedPassword(@RequestBody PasswordChangedRequest passwordChangedRequest) throws ResourceNotFoundException {

        userService.changePassword(passwordChangedRequest);
        return CommonUtil.createBuildResponseMessage("Password changed successfully!", HttpStatus.OK);
    }


}
