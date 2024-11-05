package org.sdi.productmanager.service;

import org.sdi.productmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {

    void createUser(String email);
    void deleteUser(String email);
    User getUser(String email);
}
