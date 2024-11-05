package org.sdi.productmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.entity.User;
import org.sdi.productmanager.exception.NotFoundException;
import org.sdi.productmanager.repository.UserRepository;
import org.sdi.productmanager.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String CREATE_USER_TOPIC = "user.create";
    private static final String DELETE_USER_TOPIC = "user.delete";

    private final UserRepository userRepository;

    @KafkaListener(topics = CREATE_USER_TOPIC)
    public void createUser(String email) {
        User newUser = User.builder().email(email).build();
        userRepository.save(newUser);
    }

    @KafkaListener(topics = DELETE_USER_TOPIC)
    public void deleteUser(String email) {
        userRepository.findById(email).ifPresent(userRepository::delete);
    }

    @Override
    public User getUser(String email) {
        return userRepository.findById(email).orElseThrow(() ->
                new NotFoundException(String.format("User with email %s not found", email)));
    }
}
