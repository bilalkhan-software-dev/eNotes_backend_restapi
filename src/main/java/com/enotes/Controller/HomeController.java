package com.enotes.Controller;


import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.ResetPasswordRequest;
import com.enotes.Endpoints.HomeControllerEndpoints;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.HomeService;
import com.enotes.Service.UserService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController implements HomeControllerEndpoints {

    private final HomeService homeService;
    private final UserService userService;


    @Override
    public ResponseEntity<?> verifyEmail(Integer userId, String token) throws ResourceNotFoundException {

        log.info("Home Controller : verifyEmail() Start");
        boolean isVerified = homeService.verifyEmail(userId, token);

        if (isVerified) {
            return CommonUtil.createBuildResponseMessage("Account Verified Successfully!", HttpStatus.CREATED);
        }
        log.info("Home Controller : verifyEmail() End");

        return CommonUtil.createErrorResponseMessage("Account Verification Failed!", HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<?> verifyEmailForResetPassword(String email) throws Exception {

        userService.sendResetPasswordOTP(email);
        return CommonUtil.createBuildResponseMessage("OTP send to email password for reset password request ", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> authenticateOtp(Integer Otp) {
        userService.verifyOTP(Otp);
        return CommonUtil.createBuildResponseMessage("OTP is correct,Now you can reset your password", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) throws ResourceNotFoundException {
        userService.resetPassword(request);
        return CommonUtil.createBuildResponseMessage("Password reset successfully", HttpStatus.OK);
    }

}
