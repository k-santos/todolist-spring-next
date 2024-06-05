package com.example.backend;

import com.example.backend.entities.DTOrequest.CreateTaskRequestDTO;
import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.DTOrequest.FinalizeTaskRequestDTO;
import com.example.backend.entities.DTOrequest.LoginRequestDTO;
import com.example.backend.entities.Task;
import com.example.backend.entities.TaskHistory;
import com.example.backend.repositories.ComplementRepository;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FinishTaskTest {

    private static String token;
    private static Long idTask;
    private static Long idTaskWithComplement;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryRepository taskHistoryRepository;
    @Autowired
    private ComplementRepository complementRepository;

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
    public void createTasks() throws Exception {
        complementRepository.deleteAll();
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
        idTask = tasks.get(0).getId();

        CreateTaskRequestDTO createTaskWithComplementRequestDTO = new CreateTaskRequestDTO();
        createTaskWithComplementRequestDTO.setName("Read a book");
        createTaskWithComplementRequestDTO.setUnit("pages");
        createTaskWithComplementRequestDTO.setValue(new BigDecimal(10));
        taskJson = objectMapper.writeValueAsString(createTaskWithComplementRequestDTO);

        mockMvc.perform(post("/task/create")
                        .header("Authorization", "Bearer " + token)
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        tasks = taskRepository.findAll();
        idTaskWithComplement = tasks.get(0).getId();
    }

    @Test
    @DisplayName("Should finish a task")
    public void finishTask() throws Exception {

        FinalizeTaskRequestDTO finalizeTaskRequestDTO = new FinalizeTaskRequestDTO();
        finalizeTaskRequestDTO.setTaskId(idTask);
        finalizeTaskRequestDTO.setDate(new Timestamp(System.currentTimeMillis()));

        ObjectMapper objectMapper = new ObjectMapper();
        String finalizeTaskJson = objectMapper.writeValueAsString(finalizeTaskRequestDTO);

        mockMvc.perform(post("/task/finish")
                        .header("Authorization", "Bearer " + token)
                        .content(finalizeTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        assertThat(taskHistories).isNotNull();
        assertThat(taskHistories).hasSize(1);
        assertThat(taskHistories.get(0).getTaskId()).isEqualTo(finalizeTaskRequestDTO.getTaskId());
        assertThat(taskHistories.get(0).getDate()).isEqualTo(finalizeTaskRequestDTO.getDate());
    }

    @Test
    @DisplayName("Should finish a task with complement")
    public void finishTaskWithComplement() throws Exception {

        FinalizeTaskRequestDTO finalizeTaskRequestDTO = new FinalizeTaskRequestDTO();
        finalizeTaskRequestDTO.setTaskId(idTaskWithComplement);
        finalizeTaskRequestDTO.setValue(new BigDecimal("10.5"));
        finalizeTaskRequestDTO.setDate(new Timestamp(System.currentTimeMillis()));

        ObjectMapper objectMapper = new ObjectMapper();
        String finalizeTaskJson = objectMapper.writeValueAsString(finalizeTaskRequestDTO);

        mockMvc.perform(post("/task/finish")
                        .header("Authorization", "Bearer " + token)
                        .content(finalizeTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        assertThat(taskHistories).isNotNull();
        assertThat(taskHistories).hasSize(1);
        assertThat(taskHistories.get(0).getTaskId()).isEqualTo(finalizeTaskRequestDTO.getTaskId());
        assertThat(taskHistories.get(0).getDate()).isEqualTo(finalizeTaskRequestDTO.getDate());
        assertThat(taskHistories.get(0).getValue()).isEqualTo(finalizeTaskRequestDTO.getValue());
    }

    @Test
    @DisplayName("Should not finish a task without idTask")
    public void finishTaskWithoutIdTask() throws Exception {

        FinalizeTaskRequestDTO finalizeTaskRequestDTO = new FinalizeTaskRequestDTO();
        finalizeTaskRequestDTO.setTaskId(null);
        finalizeTaskRequestDTO.setDate(new Timestamp(System.currentTimeMillis()));

        ObjectMapper objectMapper = new ObjectMapper();
        String finalizeTaskJson = objectMapper.writeValueAsString(finalizeTaskRequestDTO);

        mockMvc.perform(post("/task/finish")
                        .header("Authorization", "Bearer " + token)
                        .content(finalizeTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should not finish a task without date")
    public void finishTaskWithoutDate() throws Exception {

        FinalizeTaskRequestDTO finalizeTaskRequestDTO = new FinalizeTaskRequestDTO();
        finalizeTaskRequestDTO.setTaskId(idTask);
        finalizeTaskRequestDTO.setDate(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String finalizeTaskJson = objectMapper.writeValueAsString(finalizeTaskRequestDTO);

        mockMvc.perform(post("/task/finish")
                        .header("Authorization", "Bearer " + token)
                        .content(finalizeTaskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}