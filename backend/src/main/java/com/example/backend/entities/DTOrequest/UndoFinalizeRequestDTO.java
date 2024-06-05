package com.example.backend.entities.DTOrequest;

import jakarta.validation.constraints.NotNull;

public class UndoFinalizeRequestDTO {
    @NotNull
    private Long historyId;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }
}