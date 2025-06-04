package com.enotes.Service;

import com.enotes.Exception.ResourceNotFoundException;

public interface HomeService {

    boolean verifyEmail(Integer userId, String token) throws IllegalArgumentException, ResourceNotFoundException;
}
