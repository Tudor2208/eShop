package org.sdi.usermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.usermanager.dto.*;
import org.sdi.usermanager.entity.User;
import org.sdi.usermanager.exceptions.EmailAlreadyExistsException;
import org.sdi.usermanager.exceptions.InvalidCredentialsException;
import org.sdi.usermanager.exceptions.NotFoundException;
import org.sdi.usermanager.repository.UserRepository;
import org.sdi.usermanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsById(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        User entity = userMapper.toEntity(request);
        return userMapper.toUserResponse(userRepository.save(entity));
    }

    @Override
    public UserResponse getUser(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        return userOptional.map(userMapper::toUserResponse)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found", email)));
    }

    @Override
    public PaginatedResponse<UserResponse> getUsers(Pageable pageable) {
        Page<User> pageableUsers = userRepository.findAllByIsAdmin(false, pageable);

        List<UserResponse> users = pageableUsers.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<UserResponse>builder()
                .content(users)
                .page(pageableUsers.getNumber())
                .size(pageableUsers.getSize())
                .totalElements(pageableUsers.getTotalElements())
                .totalPages(pageableUsers.getTotalPages())
                .build();
    }

    @Override
    public UserResponse patchUser(String email, PatchUserRequest request) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found", email)));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> userOptional = userRepository.findById(email);
        User user = userOptional.orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return userMapper.toLoginResponse(user);
    }

    @Override
    public void deleteUser(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        userOptional.ifPresentOrElse(userRepository::delete, () -> {
            throw new NotFoundException(String.format("User with email %s not found", email));
        });
    }
}
