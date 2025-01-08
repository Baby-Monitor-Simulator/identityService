package com.babymonitor.identity.integrationtest;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
public class RegisterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // Perform a successful registration
    @Test
    public void registrationTestSuccess() throws Exception {
        UserDTO user = new UserDTO();
        user.setEmail("example@email.com");
        user.setPassword("password");

        String jsonBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/identity/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }


    // Perform a failing registration
    @Test
    public void registrationTestFailMissingEmail() throws Exception {
        UserDTO user = new UserDTO();
        user.setPassword("password");

        String jsonBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/identity/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError());
    }
}
