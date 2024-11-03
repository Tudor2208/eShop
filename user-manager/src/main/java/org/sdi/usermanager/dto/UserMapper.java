package org.sdi.usermanager.dto;

import lombok.RequiredArgsConstructor;
import org.sdi.usermanager.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(encrypt(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .isAdmin(false)
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    private String encrypt(String password) {
        return passwordEncoder.encode(password);
    }
}
