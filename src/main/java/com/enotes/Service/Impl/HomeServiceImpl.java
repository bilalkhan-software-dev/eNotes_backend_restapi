package com.enotes.Service.Impl;

import com.enotes.Entity.AccountStatus;
import com.enotes.Entity.User;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Repository.UserRepository;
import com.enotes.Service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final UserRepository userRepository;

    @Override
    public boolean verifyEmail(Integer userId, String token) throws IllegalArgumentException, ResourceNotFoundException {

        log.info("HomeServiceImpl : verifyEmail() Start");
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid user!")
        );
        if (user.getStatus().getVerificationCode() == null || user.getStatus().getVerificationCode().isEmpty()) {
            throw new IllegalArgumentException("User is already registered!");
        }

        if (user.getStatus().getVerificationCode().equals(token)) {
            AccountStatus status = AccountStatus.builder()
                    .isEnabled(true)
                    .verificationCode(null)
                    .build();
            user.setStatus(status);
        }else {
            throw new IllegalArgumentException("Invalid Token!");
        }

        User isVerified = userRepository.save(user);
        log.info("HomeServiceImpl : verifyEmail() End");
        return !ObjectUtils.isEmpty(isVerified);

    }
}
