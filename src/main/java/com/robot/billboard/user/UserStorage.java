package com.robot.billboard.user;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);

    Optional<User> getUser(Long userId);

}
