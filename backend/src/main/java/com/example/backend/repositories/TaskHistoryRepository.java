package com.example.backend.repositories;

import com.example.backend.entities.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    @Query("SELECT taskHistory FROM TaskHistory taskHistory WHERE taskHistory.taskId = :taskId")
    List<TaskHistory> findByTaskId(@Param("taskId") Long taskId);
}