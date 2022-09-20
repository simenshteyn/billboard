package com.robot.billboard.user;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> removeUser(Long userId);

    Optional<User> updateUser(Long userId, User user);

    Optional<User> getUser(Long userId);

    List<User> getAllUsers();
}
