package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


}
