package com.robot.billboard.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersistentUserStorage implements UserStorage {
    private final UserRepository userRepository;

    @Autowired
    public PersistentUserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> addUser(User user) {
        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }
}
