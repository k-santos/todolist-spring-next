package com.example.backend.controllers;

import com.example.backend.entities.DTOrequest.LoginResponseDTO;
import com.example.backend.entities.DTOrequest.LoginRequestDTO;
import com.example.backend.entities.User;
import com.example.backend.services.DetailServiceImpl;
import com.example.backend.services.JwtService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api")
public class AuthenticatorController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private DetailServiceImpl detailService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) throws Exception {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        } catch (AuthenticationException e) {
            throw new Exception("Invalid username or password");
        }
        UserDetails userDetails = detailService.loadUserByUsername(login.getUsername());
        String token = jwtService.generateToken(userDetails);
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getName()));
    }
}
