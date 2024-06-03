package com.example.backend.services;

import com.example.backend.entities.Complement;
import com.example.backend.entities.DTOrequest.*;
import com.example.backend.entities.DTOresponse.TaskResponseDTO;
import com.example.backend.entities.Task;
import com.example.backend.entities.TaskHistory;
import com.example.backend.entities.User;
import com.example.backend.repositories.ComplementRepository;
import com.example.backend.repositories.TaskHistoryRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComplementRepository complementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    public Task createTask(CreateTaskRequestDTO createTaskRequestDTO, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Task task = new Task();
        task.setName(createTaskRequestDTO.getName());
        task.setUserId(user.getId());

        Task savedTask = taskRepository.save(task);

        if (createTaskRequestDTO.getUnit() != null && createTaskRequestDTO.getValue() != null) {
            Complement complement = new Complement();
            complement.setUnit(createTaskRequestDTO.getUnit());
            complement.setValue(createTaskRequestDTO.getValue());
            complement.setTask(savedTask);
            savedTask.setComplement(complement);


            complementRepository.save(complement);
        }

        return taskRepository.save(savedTask);
    }

    public List<Task> listTasksByUsername(ListTaskRequestDTO listTaskRequestDTO) {
        User user = userRepository.findByUsername(listTaskRequestDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return taskRepository.findByUserId(user.getId());
    }

    public List<TaskResponseDTO> findTaskAndHistory(String username, Timestamp timestamp) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return taskRepository.findTaskAndHistory(user.getId(), timestamp);
    }

    public TaskHistory finalizeTasks(FinalizeTaskRequestDTO finalizeTaskRequestDTO) {
        Optional<Task> taskOptional = taskRepository.findById(finalizeTaskRequestDTO.getTaskId());
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Task task = taskOptional.get();
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setDate(finalizeTaskRequestDTO.getDate());
        taskHistory.setValue(finalizeTaskRequestDTO.getValue());
        taskHistory.setTaskId(task.getId());
        TaskHistory persisted = taskHistoryRepository.save(taskHistory);
        return persisted;
    }

    public void undoFinalizeTask(UndoFinalizeRequestDTO undoFinalizeRequestDTO) {
        taskHistoryRepository.deleteById(undoFinalizeRequestDTO.getHistoryId());
    }

    public List<TaskHistory> listTaskHistories(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Task task = taskOptional.get();

        List<TaskHistory> listTaskHistory = taskHistoryRepository.findByTaskId(task.getId());
        return listTaskHistory;
    }
}