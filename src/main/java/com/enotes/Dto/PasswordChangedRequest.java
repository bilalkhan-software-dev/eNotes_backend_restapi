package com.enotes.Dto;


import lombok.Data;

@Data
public class PasswordChangedRequest {

    private String oldPassword;
    private String newPassword;

}
