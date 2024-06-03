package com.example.backend.entities.DTOrequest;

import java.sql.Timestamp;

public class ListTaskRequestDTO {
    private String username;
    private Timestamp date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}