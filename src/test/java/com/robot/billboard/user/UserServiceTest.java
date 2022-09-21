package com.robot.billboard.user;

import com.robot.billboard.security.JWTToken;
import com.robot.billboard.security.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserStorage userStorage;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void signupUser() {
        User user = new User();
        user.setPassword("test password");
        user.setEmail("test@email.com");
        user.setRole("ROLE_USER");
        when(jwtUtil.generateToken(Mockito.anyString())).thenReturn("valid_token");
        when(userStorage.addUser(any(User.class))).thenReturn(Optional.of(user));
        JWTToken result = userService.signupUser(user);
        assertEquals("test@email.com", result.getUserEmail());
        assertEquals("valid_token", result.getAccessToken());
    }

    @Test
    void loginUser() {
        User user = new User();
        user.setPassword("test pass");
        user.setEmail("test@email.ru");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(jwtUtil.generateToken(Mockito.anyString())).thenReturn("valid_token");
        JWTToken result = userService.loginUser(user);
        assertEquals("test@email.ru", result.getUserEmail());
        assertEquals("valid_token", result.getAccessToken());
    }
}