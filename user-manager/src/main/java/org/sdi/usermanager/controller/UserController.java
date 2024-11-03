package org.sdi.usermanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.usermanager.dto.CreateUserRequest;
import org.sdi.usermanager.dto.PaginatedResponse;
import org.sdi.usermanager.dto.PatchUserRequest;
import org.sdi.usermanager.dto.UserResponse;
import org.sdi.usermanager.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.sdi.usermanager.Constants.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + USERS)
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity.created(URI.create(V1 + USERS + SLASH + userResponse.getEmail())).body(userResponse);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable(value="email") String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserResponse>> getUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                                                    @RequestParam(value="size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(value="email") String email,
                                                   @Valid @RequestBody PatchUserRequest request) {
        return ResponseEntity.ok(userService.patchUser(email, request));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value="email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
