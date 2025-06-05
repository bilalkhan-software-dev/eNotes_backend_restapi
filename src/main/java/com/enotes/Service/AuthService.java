package com.enotes.Service;

import com.enotes.Dto.LoginRequest;
import com.enotes.Dto.LoginResponse;
import com.enotes.Dto.UserDto;

public interface AuthService {

    boolean registerUser(UserDto userDto) throws Exception;

    LoginResponse authenticateLoginUser(LoginRequest loginRequest);


}
