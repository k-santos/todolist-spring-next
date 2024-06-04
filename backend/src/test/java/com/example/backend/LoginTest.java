package com.example.backend;

import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.DTOrequest.LoginRequestDTO;
import com.example.backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.jayway.jsonpath.JsonPath.parse;
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();

	}



	@Test
	@DisplayName("should login successfully")
	public void testLogin() throws Exception {
		CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
		userDTO.setName("Name");
		userDTO.setUsername("Username");
		userDTO.setPassword("password");

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(userDTO);

		mockMvc.perform(post("/user/create")
						.content(userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setUsername(userDTO.getUsername());
		loginRequestDTO.setPassword(userDTO.getPassword());

		String loginJson = objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc.perform(post("/api/login")
						.content(loginJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String responseContent = result.getResponse().getContentAsString();

		String token = parse(responseContent).read("$.token", String.class);
		assertThat(token).isNotNull();
	}

}