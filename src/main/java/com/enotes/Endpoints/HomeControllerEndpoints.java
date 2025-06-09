package com.enotes.Endpoints;

import com.enotes.Dto.ResetPasswordRequest;
import com.enotes.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Home",description = "Home page")
@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoints {

    @Operation(summary = "Verify registered user email",tags = {"Home"})
    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam Integer userId, @RequestParam String token) throws ResourceNotFoundException;

    @Operation(summary = "Forgot password request email",tags = {"Home"})
    @PostMapping("/forgot-password")
    public ResponseEntity<?> verifyEmailForResetPassword(@RequestParam String email) throws Exception;

    @Operation(summary = "Verify Forgot password request OTP ",tags = {"Home"})
    @GetMapping("/verifyOTP")
    public ResponseEntity<?> authenticateOtp(@RequestParam("otp") Integer Otp);

    @Operation(summary = "Reset password ",tags = {"Home"})
    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) throws ResourceNotFoundException;




}
