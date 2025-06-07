package com.enotes.Endpoints;

import com.enotes.Dto.ResetPasswordRequest;
import com.enotes.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoints {

    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam Integer userId, @RequestParam String token) throws ResourceNotFoundException;


    @PostMapping("/forgot-password")
    public ResponseEntity<?> verifyEmailForResetPassword(@RequestParam String email) throws Exception;

    @GetMapping("/verifyOTP")
    public ResponseEntity<?> authenticateOtp(@RequestParam("otp") Integer Otp);

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) throws ResourceNotFoundException;




}
