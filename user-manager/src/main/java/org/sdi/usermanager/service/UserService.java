package org.sdi.usermanager.service;

import org.sdi.usermanager.dto.CreateUserRequest;
import org.sdi.usermanager.dto.PaginatedResponse;
import org.sdi.usermanager.dto.PatchUserRequest;
import org.sdi.usermanager.dto.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUser(String email);
    PaginatedResponse<UserResponse> getUsers(Pageable pageable);
    UserResponse patchUser(String email, PatchUserRequest request);
    void deleteUser(String email);
}
