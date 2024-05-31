package com.example.backend.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Complement")
public class Complement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "complement_seq")
    @SequenceGenerator(name = "complement_seq", sequenceName = "complement_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, precision = 65, scale = 30)
    private BigDecimal value;

    @Column(nullable = false)
    private String unit;

    @Column(name = "taskId", nullable = false)
    private Long taskId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}