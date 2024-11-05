package org.sdi.usermanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.usermanager.dto.*;
import org.sdi.usermanager.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

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
    public ResponseEntity<PaginatedResponse<UserResponse>> getUsers(@RequestParam(value="page", defaultValue="0") int page,
                                                                    @RequestParam(value="size", defaultValue="10") int size,
                                                                    @RequestParam(value="sortBy", defaultValue="lastName") String sortBy,
                                                                    @RequestParam(value="dir", defaultValue="asc") String dir) {
        Sort.Direction direction = dir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserResponse> patchUser(@PathVariable(value="email") String email,
                                                   @Valid @RequestBody PatchUserRequest request) {
        return ResponseEntity.ok(userService.patchUser(email, request));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value="email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
