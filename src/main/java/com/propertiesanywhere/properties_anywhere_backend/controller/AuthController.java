package com.propertiesanywhere.properties_anywhere_backend.controller;

import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        return authService.login(
                user.getEmail(),
                user.getPassword()
        );
    }
}