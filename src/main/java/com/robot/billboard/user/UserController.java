package com.robot.billboard.user;

import com.robot.billboard.validator.OnCreate;
import com.robot.billboard.validator.OnLogin;
import com.robot.billboard.validator.ValidationErrorBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Sign up new user to work with API")
    @PostMapping("/signup")
    @Validated(OnCreate.class)
    public ResponseEntity<?> signupUser(HttpServletRequest request, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(userService.signupUser(user));
    }

    @Operation(summary = "Login user with email and password to obtain JWT access token")
    @PostMapping("/login")
    @Validated(OnLogin.class)
    public ResponseEntity<?> loginUser(HttpServletRequest request, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(userService.loginUser(user));
    }

    @GetMapping("/hello")
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok("hello");
    }
}
