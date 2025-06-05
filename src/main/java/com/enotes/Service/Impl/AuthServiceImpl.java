package com.enotes.Service.Impl;

import com.enotes.Config.Security.CustomUserDetail;
import com.enotes.Dto.LoginRequest;
import com.enotes.Dto.LoginResponse;
import com.enotes.Dto.UserDto;
import com.enotes.Dto.UserDto.RoleDto;
import com.enotes.Dto.UserResponse;
import com.enotes.Entity.AccountStatus;
import com.enotes.Entity.Role;
import com.enotes.Entity.User;
import com.enotes.Exception.ExistDataException;
import com.enotes.Repository.RoleRepository;
import com.enotes.Repository.UserRepository;
import com.enotes.Service.AuthService;
import com.enotes.Service.EmailService;
import com.enotes.Service.JwtService;
import com.enotes.Util.CommonUtil;
import com.enotes.Util.EmailSendingTemplate;
import com.enotes.Util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final CommonUtil commonUtil;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public boolean registerUser(UserDto userDto) throws Exception {

        validation.UserValidation(userDto);

        String email = userDto.getEmail();

        boolean isExist = userRepository.existsByEmail(email);
        if (isExist){
            throw new ExistDataException("Email is already in use! Try with another email");
        }

        User user = modelMapper.map(userDto, User.class);

        setRole(userDto,user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String token = UUID.randomUUID().toString();
        AccountStatus status = AccountStatus.builder()
                .isEnabled(false)
                .verificationCode(token)
                .resetPasswordCode(null)
                .build();

        user.setStatus(status);
        User isSaved = userRepository.save(user);

        String username = isSaved.getEmail();
        Integer userId = isSaved.getId();

        if (!ObjectUtils.isEmpty(isSaved)){
            String generatedUrl = commonUtil.generateUrl(userId, token);
            String content = EmailSendingTemplate.sendEmailForVerification(isSaved.getFirstName(),generatedUrl);
            emailService.sendMail(username,"Email Verification",content);
            return true;
        }

        return false;
    }

    private void setRole(UserDto userDto, User user) {

        List<Integer> requestedRoleList = userDto.getRoles().stream().map(RoleDto::getId).toList();
        List<Role> isRoleFind = roleRepository.findAllById(requestedRoleList);

        user.setRoles(isRoleFind);

    }

    @Override
    public LoginResponse authenticateLoginUser(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authenticate.isAuthenticated()){
            CustomUserDetail userDetail = (CustomUserDetail) authenticate.getPrincipal();
            String token = jwtService.generateToken(userDetail.getUser());
            return LoginResponse.builder()
                    .userResponse(modelMapper.map(userDetail.getUser(), UserResponse.class))
                    .token(token)
                    .build();
        }

        return null;
    }
}
