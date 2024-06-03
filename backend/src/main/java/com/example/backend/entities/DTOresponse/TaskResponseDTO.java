package com.example.backend.entities.DTOresponse;

import java.math.BigDecimal;

public class TaskResponseDTO {
    private Long id;
    private String name;
    private String complement;
    private Long idHistoryToday;

    public TaskResponseDTO(Long id, String name, String complement, Long idHistoryToday) {
        this.id = id;
        this.name = name;
        this.complement = complement;
        this.idHistoryToday = idHistoryToday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public Long getIdHistoryToday() {
        return idHistoryToday;
    }

    public void setIdHistoryToday(Long idHistoryToday) {
        this.idHistoryToday = idHistoryToday;
    }
}
