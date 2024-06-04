package com.example.backend;

import com.example.backend.entities.DTOrequest.CreateTaskRequestDTO;
import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.DTOrequest.LoginRequestDTO;
import com.example.backend.entities.Task;
import com.example.backend.repositories.ComplementRepository;
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
import java.util.List;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateTaskTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ComplementRepository complementRepository;

	private static String token;

	@BeforeAll
	public static void setUp(@Autowired MockMvc mockMvc,
							 @Autowired UserRepository userRepository,
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
	public void cleanUp() {
		complementRepository.deleteAll();
		taskRepository.deleteAll();
	}

	@Test
	@DisplayName("Should create a task")
	public void testCreateTask() throws Exception {
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

		List<Task> tasks =  taskRepository.findAll();
		assertThat(tasks).isNotNull();
		assertThat(tasks).hasSize(1);
		assertThat(tasks.get(0).getName()).isEqualTo(createTaskRequestDTO.getName());
	}

	@Test
	@DisplayName("Should create a task with complement")
	public void testCreateTaskWithComplement() throws Exception {
		CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO();
		createTaskRequestDTO.setName("Read a book");
		createTaskRequestDTO.setUnit("pages");
		createTaskRequestDTO.setValue(new BigDecimal(10.5));

		ObjectMapper objectMapper = new ObjectMapper();
		String taskJson = objectMapper.writeValueAsString(createTaskRequestDTO);

		mockMvc.perform(post("/task/create")
						.header("Authorization", "Bearer " + token)
						.content(taskJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		List<Task> tasks =  taskRepository.findAll();
		assertThat(tasks).isNotNull();
		assertThat(tasks).hasSize(1);
		assertThat(tasks.get(0).getName()).isEqualTo(createTaskRequestDTO.getName());
		assertThat(tasks.get(0).getComplement().getUnit()).isEqualTo(createTaskRequestDTO.getUnit());
		assertThat(tasks.get(0).getComplement().getValue()).isEqualTo(createTaskRequestDTO.getValue());
	}

	@Test
	@DisplayName("Should not create a task without name")
	public void testCreateTaskWithoutName() throws Exception {
		CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO();
		createTaskRequestDTO.setName(null);
		createTaskRequestDTO.setUnit("pages");
		createTaskRequestDTO.setValue(new BigDecimal(10.5));

		ObjectMapper objectMapper = new ObjectMapper();
		String taskJson = objectMapper.writeValueAsString(createTaskRequestDTO);

		mockMvc.perform(post("/task/create")
						.header("Authorization", "Bearer " + token)
						.content(taskJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}