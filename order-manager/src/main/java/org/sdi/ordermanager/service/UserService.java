package org.sdi.ordermanager.service;

import org.springframework.stereotype.Component;

@Component
public interface UserService {
    void createUser(String email);
    void deleteUser(String email);
}
