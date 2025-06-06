package com.enotes.Controller;


import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.ResetPasswordRequest;
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
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;
    private final UserService userService;

    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam Integer userId, @RequestParam String token) throws ResourceNotFoundException {

        log.info("Home Controller : verifyEmail() Start");
        boolean isVerified = homeService.verifyEmail(userId, token);

        if (isVerified) {
            return CommonUtil.createBuildResponseMessage("Account Verified Successfully!", HttpStatus.CREATED);
        }
        log.info("Home Controller : verifyEmail() End");

        return CommonUtil.createErrorResponseMessage("Account Verification Failed!", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> verifyEmailForResetPassword(@RequestParam String email) throws Exception {

        userService.sendResetPasswordOTP(email);
        return CommonUtil.createBuildResponseMessage("OTP send to email password for reset password request ",HttpStatus.OK);
    }

    @GetMapping("/verifyOTP")
    public ResponseEntity<?> authenticateOtp(@RequestParam("otp") Integer Otp){
        userService.verifyOTP(Otp);
        return CommonUtil.createBuildResponseMessage("OTP is correct,Now you can reset your password",HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) throws ResourceNotFoundException {
        userService.resetPassword(request);
        return CommonUtil.createBuildResponseMessage("Password reset successfully",HttpStatus.OK);
    }

}
