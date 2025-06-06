package com.enotes.Dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;
    private String newPassword;

}
