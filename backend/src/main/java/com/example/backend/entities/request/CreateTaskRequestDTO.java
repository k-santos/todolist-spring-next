package com.example.backend.entities.request;

import java.math.BigDecimal;

public class CreateTaskRequestDTO {
    private String username;
    private String taskName;
    private String complementUnit;
    private BigDecimal complementValue;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComplementUnit() {
        return complementUnit;
    }

    public void setComplementUnit(String complementUnit) {
        this.complementUnit = complementUnit;
    }

    public BigDecimal getComplementValue() {
        return complementValue;
    }

    public void setComplementValue(BigDecimal complementValue) {
        this.complementValue = complementValue;
    }
}