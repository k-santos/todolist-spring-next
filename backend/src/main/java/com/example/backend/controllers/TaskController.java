package com.example.backend.controllers;

import com.example.backend.entities.Task;
import com.example.backend.entities.request.CreateTaskRequestDTO;
import com.example.backend.entities.request.ListTaskRequestDTO;
import com.example.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(
            @RequestBody CreateTaskRequestDTO createTaskRequestDTO) {
        Task createdTask = taskService.createTask(createTaskRequestDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> listTasksByUser(@RequestBody ListTaskRequestDTO listTaskRequestDTO) {
        List<Task> tasks = taskService.listTasksByUsername(listTaskRequestDTO.getUsername());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}