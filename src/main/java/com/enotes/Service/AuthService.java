package com.enotes.Service;

import com.enotes.Dto.UserDto;

public interface AuthService {

    boolean registerUser(UserDto userDto) throws Exception;


}
