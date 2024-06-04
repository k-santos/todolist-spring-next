package com.example.backend.controllers;

import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        User createdUser = userService.createUser(createUserRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}