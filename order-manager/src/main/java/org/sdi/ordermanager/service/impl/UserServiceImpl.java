package org.sdi.ordermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.entity.Cart;
import org.sdi.ordermanager.entity.User;
import org.sdi.ordermanager.repository.UserRepository;
import org.sdi.ordermanager.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String CREATE_USER_TOPIC = "user.create";
    private static final String DELETE_USER_TOPIC = "user.delete";

    private final UserRepository userRepository;

    @KafkaListener(topics = CREATE_USER_TOPIC)
    public void createUser(String email) {
        User newUser = User.builder().email(email).build();
        Cart cart = new Cart();
        cart.setUser(newUser);
        newUser.setCart(cart);
        userRepository.save(newUser);
    }

    @KafkaListener(topics = DELETE_USER_TOPIC)
    public void deleteUser(String email) {
        userRepository.findById(email).ifPresent(userRepository::delete);
    }
}
