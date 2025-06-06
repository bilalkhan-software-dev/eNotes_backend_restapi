package com.enotes.Service.Impl;

import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.ResetPasswordRequest;
import com.enotes.Entity.User;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Exception.SuccessException;
import com.enotes.Repository.UserRepository;
import com.enotes.Service.EmailService;
import com.enotes.Service.UserService;
import com.enotes.Util.CommonUtil;
import com.enotes.Util.EmailSendingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final int OTP_EXPIRATION_MINUTES = 15;
    private static final Integer Password_Final_Length = 6;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();


    @Override
    @Transactional
    public void changePassword(PasswordChangedRequest request) throws ResourceNotFoundException {
        String currentUsername = CommonUtil.GetLoggedInUserDetails().getEmail();
        log.info("Changing password for user: {}", currentUsername);

        User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }
        if (request.getNewPassword().length() < Password_Final_Length){
            throw new IllegalArgumentException("Password must contain 6 characters");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", currentUsername);
    }

    @Override
    public void sendResetPasswordOTP(String email) throws Exception {
        log.info("Sending reset password OTP to email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email)
                );

        int otp = generateOTP();
        log.debug("Generated OTP for user {}: {}", user.getEmail(), otp);

        String emailContent = EmailSendingTemplate.sendEmailForPasswordReset(user.getFirstName(), otp);
        emailService.sendMail(user.getEmail(), "Reset Your eNotes Password", emailContent);

        user.getStatus().setResetPasswordCode(otp);
        user.getStatus().setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);

        log.info("Reset password OTP sent successfully to: {}", email);
    }

    @Override
    public void verifyOTP(Integer otp) {
        log.info("Verifying OTP: {}", otp);

        User user = userRepository.findByStatus_ResetPasswordCode(otp)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP provided"));

        if (isOtpExpired(user.getStatus().getOtpGeneratedTime())) {
            user.getStatus().setResetPasswordCode(null);
            user.getStatus().setOtpGeneratedTime(null);
            userRepository.save(user);
            throw new IllegalArgumentException("OTP has expired");
        }

        user.getStatus().setOtpGeneratedTime(null);
        userRepository.save(user);

        log.info("OTP verified successfully for user: {}", user.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) throws ResourceNotFoundException {
        log.info("Resetting password for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus().getResetPasswordCode() == null) {
            throw new IllegalArgumentException("Password reset not initiated or OTP not verified");
        }

        if (Objects.equals(user.getPassword(), request.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.getStatus().setResetPasswordCode(null);
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", request.getEmail());
    }

    private int generateOTP() {
        return 100000 + random.nextInt(900000);
    }

    private boolean isOtpExpired(LocalDateTime otpGeneratedTime) {
        return otpGeneratedTime == null ||
                otpGeneratedTime.plusMinutes(OTP_EXPIRATION_MINUTES).isBefore(LocalDateTime.now());
    }
}