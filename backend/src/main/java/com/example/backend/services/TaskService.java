package com.example.backend.services;

import com.example.backend.entities.Complement;
import com.example.backend.entities.Task;
import com.example.backend.entities.User;
import com.example.backend.entities.request.TaskRequestDTO;
import com.example.backend.repositories.ComplementRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComplementRepository complementRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(TaskRequestDTO taskRequestDTO) {
        User user = userRepository.findByUsername(taskRequestDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Task task = new Task();
        task.setName(taskRequestDTO.getTaskName());
        task.setUserId(user.getId());

        Task savedTask = taskRepository.save(task);

        if (taskRequestDTO.getComplementUnit() != null && taskRequestDTO.getComplementValue() != null) {
            Complement complement = new Complement();
            complement.setUnit(taskRequestDTO.getComplementUnit());
            complement.setValue(taskRequestDTO.getComplementValue());
            complement.setTask(savedTask);
            savedTask.setComplement(complement);


            complementRepository.save(complement);
        }

        return taskRepository.save(savedTask);
    }
}