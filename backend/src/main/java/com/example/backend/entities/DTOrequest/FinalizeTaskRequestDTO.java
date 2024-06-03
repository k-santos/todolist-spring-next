package com.example.backend.entities.DTOrequest;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FinalizeTaskRequestDTO {
    private Long taskId;
    private BigDecimal value;

    private Timestamp date;
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}