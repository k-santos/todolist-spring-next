package com.example.backend;

import com.example.backend.entities.DTOrequest.CreateUserRequestDTO;
import com.example.backend.entities.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a user")
    public void testCreateUser() throws Exception {
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


        User user = userRepository.findByUsername(userDTO.getUsername());
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(userDTO.getName());
        assertThat(user.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    @DisplayName("Should not create a user without name")
    public void testCreateUserWithoutName() throws Exception {
        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setName(null);
        userDTO.setUsername("Username");
        userDTO.setPassword("password");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should not create a user without username")
    public void testCreateUserWithoutUsername() throws Exception {
        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setName("Name");
        userDTO.setUsername(null);
        userDTO.setPassword("password");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should not create a user without password")
    public void testCreateUserWithoutPassword() throws Exception {
        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setName("Name");
        userDTO.setUsername("Username");
        userDTO.setPassword(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should not create a user when the password is less than 8 characteres")
    public void testCreateUserWithShortPassword() throws Exception {

        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setName("Name");
        userDTO.setUsername("Username");
        userDTO.setPassword("1234567");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}