package com.robot.billboard.user;

import com.robot.billboard.security.JWTToken;
import com.robot.billboard.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserStorage userStorage, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userStorage = userStorage;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public JWTToken signupUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registratedUser = userStorage.addUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add user")
        );
        String token = jwtUtil.generateToken(registratedUser.getEmail());
        return new JWTToken(registratedUser.getEmail(), token);
    }

    public JWTToken loginUser(User user) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new JWTToken(user.getEmail(), token);
    }
}
