package com.babymonitor.identity.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
public class LoginIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void logintestSucces() throws Exception {
        LoginRequest loginRequest = new LoginRequest("adminbm", "myadminpassword");

        String jsonBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/identity/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }
}
