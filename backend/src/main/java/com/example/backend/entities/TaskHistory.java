package com.example.backend.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.*;
@Entity
@Table(name = "TaskHistory")
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskhistory_seq")
    @SequenceGenerator(name = "taskhistory_seq", sequenceName = "taskhistory_seq", allocationSize = 1)
    private Long id;

    @Column(name = "taskId", nullable = false)
    private Long taskId;

    @Column(precision = 65, scale = 30)
    private BigDecimal value;

    @Column(nullable = false)
    private Timestamp date;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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