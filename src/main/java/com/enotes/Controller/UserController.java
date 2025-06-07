package com.enotes.Controller;


import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.UserResponse;
import com.enotes.Endpoints.UserControllerEndpoints;
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
@RequiredArgsConstructor
public class UserController implements UserControllerEndpoints {

    private final UserService userService;
    private final ModelMapper mapper;


    @Override
    public ResponseEntity<?> profile()  {
        User user = CommonUtil.GetLoggedInUserDetails();
        UserResponse userDetails = mapper.map(user, UserResponse.class);
        return CommonUtil.createBuildResponse(userDetails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changedPassword(PasswordChangedRequest passwordChangedRequest) throws ResourceNotFoundException {

        userService.changePassword(passwordChangedRequest);
        return CommonUtil.createBuildResponseMessage("Password changed successfully!", HttpStatus.OK);
    }


}
