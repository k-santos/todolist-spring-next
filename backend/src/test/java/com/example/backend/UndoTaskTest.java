package com.example.backend;

import com.example.backend.entities.DTOrequest.*;
import com.example.backend.entities.Task;
import com.example.backend.entities.TaskHistory;
import com.example.backend.repositories.TaskHistoryRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UndoTaskTest {

    private static String token;
    private static Long idHistory;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @BeforeAll
    public static void setUp(@Autowired MockMvc mockMvc,
                             @Autowired UserRepository userRepository,
                             @Autowired TaskRepository taskRepository,
                             @Autowired ObjectMapper objectMapper) throws Exception {
        userRepository.deleteAll();

        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO();
        createUserRequestDTO.setName("Test User");
        createUserRequestDTO.setUsername("testuser");
        createUserRequestDTO.setPassword("password");

        String userJson = objectMapper.writeValueAsString(createUserRequestDTO);

        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("testuser");
        loginRequestDTO.setPassword("password");

        String loginJson = objectMapper.writeValueAsString(loginRequestDTO);

        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .content(loginJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        token = parse(responseContent).read("$.token", String.class);


    }

    @BeforeEach
    public void createAndFinishTask() throws Exception {
        taskRepository.deleteAll();
        taskHistoryRepository.deleteAll();
        CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO();
        createTaskRequestDTO.setName("Go to the gym");
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(createTaskRequestDTO);

        mockMvc.perform(post("/task/create")
                        .header("Authorization", "Bearer " + token)
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Task> tasks = taskRepository.findAll();
        Long idTask = tasks.get(0).getId();

        FinalizeTaskRequestDTO finalizeTaskRequestDTO = new FinalizeTaskRequestDTO();
        finalizeTaskRequestDTO.setTaskId(idTask);
        finalizeTaskRequestDTO.setDate(new Timestamp(System.currentTimeMillis()));

        String finalizeTaskJson = objectMapper.writeValueAsString(finalizeTaskRequestDTO);

        mockMvc.perform(post("/task/finish")
                        .header("Authorization", "Bearer " + token)
                        .content(finalizeTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        idHistory = taskHistories.get(0).getId();
    }

    @Test
    @DisplayName("Should undo the task")
    public void undoTask() throws Exception {

        UndoFinalizeRequestDTO undoFinalizeRequestDTO = new UndoFinalizeRequestDTO();
        undoFinalizeRequestDTO.setHistoryId(idHistory);

        ObjectMapper objectMapper = new ObjectMapper();
        String undoTaskJson = objectMapper.writeValueAsString(undoFinalizeRequestDTO);

        mockMvc.perform(post("/task/undo")
                        .header("Authorization", "Bearer " + token)
                        .content(undoTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        assertThat(taskHistories).isNotNull();
        assertThat(taskHistories).hasSize(0);
    }

    @Test
    @DisplayName("Should not undo the task without historyId")
    public void undoTaskWithoutHistoryId() throws Exception {

        UndoFinalizeRequestDTO undoFinalizeRequestDTO = new UndoFinalizeRequestDTO();
        undoFinalizeRequestDTO.setHistoryId(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String undoTaskJson = objectMapper.writeValueAsString(undoFinalizeRequestDTO);

        mockMvc.perform(post("/task/undo")
                        .header("Authorization", "Bearer " + token)
                        .content(undoTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        assertThat(taskHistories).isNotNull();
        assertThat(taskHistories).hasSize(1);
    }

}