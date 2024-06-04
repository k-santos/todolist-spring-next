package com.example.backend.controllers;

import com.example.backend.entities.DTOrequest.*;
import com.example.backend.entities.DTOresponse.TaskResponseDTO;
import com.example.backend.entities.Task;
import com.example.backend.entities.TaskHistory;
import com.example.backend.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@Valid
            @RequestBody CreateTaskRequestDTO createTaskRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }

        if (username == null) {
            throw new IllegalStateException("User is not authenticated");
        }


        Task createdTask = taskService.createTask(createTaskRequestDTO, username);
        return new ResponseEntity<>(createdTask, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<TaskResponseDTO>> listTasksByUser(
            @RequestParam("date") String date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }

        if (username == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        Timestamp timestamp = Timestamp.from(localDateTime.toInstant(ZoneOffset.UTC));

        List<TaskResponseDTO> tasksAndHistory = taskService.findTaskAndHistory(username, timestamp);
        return new ResponseEntity<>(tasksAndHistory, HttpStatus.OK);
    }

    @PostMapping("/finish")
    public ResponseEntity<TaskHistory> finalizeTasks(@RequestBody FinalizeTaskRequestDTO finalizeTaskRequestDTO) {
        TaskHistory taskHistory = taskService.finalizeTasks(finalizeTaskRequestDTO);
        return new ResponseEntity<>(taskHistory, HttpStatus.OK);
    }

    @PostMapping("/undo")
    public ResponseEntity<Void> undoFinalizeTask(@RequestBody UndoFinalizeRequestDTO undoFinalizeRequestDTO) {
        taskService.undoFinalizeTask(undoFinalizeRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/history/{taskId}")
    public ResponseEntity<List<TaskHistory>> listTaskHistory(@PathVariable("taskId") Long taskId) {
        List<TaskHistory> taskHistory = taskService.listTaskHistories(taskId);
        return new ResponseEntity<>(taskHistory, HttpStatus.OK);
    }
}