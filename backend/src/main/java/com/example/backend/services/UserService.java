package com.example.backend.services;

import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequestDTO createUserRequestDTO) {
        String encoded = passwordEncoder.encode(createUserRequestDTO.getPassword());
        User user = new User();
        user.setUsername(createUserRequestDTO.getUsername());
        user.setName(createUserRequestDTO.getName());
        user.setPassword(encoded);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }
}