package org.sdi.usermanager.service;

import org.sdi.usermanager.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUser(String email);
    PaginatedResponse<UserResponse> getUsers(Pageable pageable);
    UserResponse patchUser(String email, PatchUserRequest request);
    LoginResponse loginUser(LoginRequest request);
    void deleteUser(String email);
}
