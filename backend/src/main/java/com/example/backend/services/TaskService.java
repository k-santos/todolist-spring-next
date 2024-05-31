package com.example.backend.services;

import com.example.backend.entities.Complement;
import com.example.backend.entities.Task;
import com.example.backend.entities.User;
import com.example.backend.entities.request.CreateTaskRequestDTO;
import com.example.backend.repositories.ComplementRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComplementRepository complementRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(CreateTaskRequestDTO createTaskRequestDTO) {
        User user = userRepository.findByUsername(createTaskRequestDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Task task = new Task();
        task.setName(createTaskRequestDTO.getTaskName());
        task.setUserId(user.getId());

        Task savedTask = taskRepository.save(task);

        if (createTaskRequestDTO.getComplementUnit() != null && createTaskRequestDTO.getComplementValue() != null) {
            Complement complement = new Complement();
            complement.setUnit(createTaskRequestDTO.getComplementUnit());
            complement.setValue(createTaskRequestDTO.getComplementValue());
            complement.setTask(savedTask);
            savedTask.setComplement(complement);


            complementRepository.save(complement);
        }

        return taskRepository.save(savedTask);
    }

    public List<Task> listTasksByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return taskRepository.findByUserId(user.getId());
    }
}