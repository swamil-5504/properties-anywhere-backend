package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean correctPassword =
                passwordEncoder.matches(
                        password,
                        user.getPassword()
                );

        if (!correctPassword) {
            throw new RuntimeException("Wrong password");
        }

        return user;
    }
}