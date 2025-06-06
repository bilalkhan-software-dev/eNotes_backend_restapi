package com.enotes.Service;

import com.enotes.Dto.PasswordChangedRequest;
import com.enotes.Dto.ResetPasswordRequest;
import com.enotes.Exception.ResourceNotFoundException;

public interface UserService {

    void changePassword(PasswordChangedRequest passwordChangedRequest) throws ResourceNotFoundException;
    void sendResetPasswordOTP(String email) throws Exception;
    void verifyOTP(Integer OTP);
    void resetPassword(ResetPasswordRequest resetPasswordRequest) throws ResourceNotFoundException;
}
