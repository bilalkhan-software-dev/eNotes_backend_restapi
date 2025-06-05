package com.enotes.Config.Security;

import com.enotes.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {


        return new CustomUserDetail(userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid username or not found")
        ));
    }
}
