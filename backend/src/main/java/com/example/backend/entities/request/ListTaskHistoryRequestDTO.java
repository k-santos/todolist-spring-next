package com.example.backend.entities.request;

public class ListTaskHistoryRequestDTO {
    private Long taskId;

    public Long getTasId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}