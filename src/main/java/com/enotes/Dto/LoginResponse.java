package com.enotes.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private UserResponse userResponse;

    private String token;
}
