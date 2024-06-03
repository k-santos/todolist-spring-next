package com.example.backend.repositories;

import com.example.backend.entities.DTOresponse.TaskResponseDTO;
import com.example.backend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT task FROM Task task WHERE task.userId = :userId")
    List<Task> findByUserId(@Param("userId") Long userId);


    @Query("SELECT new com.example.backend.entities.DTOresponse.TaskResponseDTO(" +
            "task.id, task.name, CONCAT(complement.value, ' ', complement.unit), taskHistory.id) " +
            "FROM Task task " +
            "LEFT JOIN TaskHistory taskHistory ON task.id = taskHistory.taskId " +
            "LEFT JOIN Complement complement ON task.id = complement.task.id " +
            "WHERE task.userId = :userId " +
            "AND (taskHistory.date IS NULL OR " +
            "(EXTRACT(YEAR FROM taskHistory.date) = EXTRACT(YEAR FROM CAST(:timestamp AS DATE)) " +
            "AND EXTRACT(MONTH FROM taskHistory.date) = EXTRACT(MONTH FROM CAST(:timestamp AS DATE)) " +
            "AND EXTRACT(DAY FROM taskHistory.date) = EXTRACT(DAY FROM CAST(:timestamp AS DATE))))")
    List<TaskResponseDTO> findTaskAndHistory(@Param("userId") Long userId, @Param("timestamp") Timestamp timestamp);

}