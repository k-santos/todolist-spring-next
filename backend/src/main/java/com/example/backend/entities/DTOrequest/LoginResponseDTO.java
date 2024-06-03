package com.example.backend.entities.DTOrequest;

public class LoginResponseDTO {
    private final String token;

    private String name;

    public LoginResponseDTO(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}