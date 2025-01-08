package com.babymonitor.identity.integrationtest;

import com.babymonitor.identity.models.RoleAssigning;
import com.babymonitor.identity.models.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
public class RoleAssignmentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // Perform a successful role assignment by an admin user
    @Test
    @WithMockUser(roles = {"admin"})
    public void roleAssignmentTestSuccess() throws Exception {
        UUID uuid = UUID.fromString("74f31995-ed32-4028-b76a-90e265d904be");
        RoleAssigning roleAssigning = new RoleAssigning();
        roleAssigning.setRoleName("instructor");
        roleAssigning.setUserID(uuid);

        String jsonBody = objectMapper.writeValueAsString(roleAssigning);

        mockMvc.perform(post("/users/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    // Perform a failing role assignment by a non-admin user
    @WithMockUser(roles = {"user"})
    @Test
    public void roleAssignmentTestFailNonAdmin() throws Exception {
        UUID uuid = UUID.fromString("74f31995-ed32-4028-b76a-90e265d904be");
        RoleAssigning roleAssigning = new RoleAssigning();
        roleAssigning.setRoleName("instructor");
        roleAssigning.setUserID(uuid);

        String jsonBody = objectMapper.writeValueAsString(roleAssigning);

        mockMvc.perform(post("/users/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());
    }

//    // Perform a failing role assignment on a non-existing user
//    @WithMockUser(roles = {"admin"})
//    @Test
//    public void roleAssignmentTestFailNonExisting() throws Exception {
//        UUID uuid = UUID.randomUUID();
//        RoleAssigning roleAssigning = new RoleAssigning();
//        roleAssigning.setRoleName("instructor");
//        roleAssigning.setUserID(uuid);
//
//        String jsonBody = objectMapper.writeValueAsString(roleAssigning);
//
//        mockMvc.perform(post("/users/role")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonBody))
//                .andExpect(status().isOk());
//    }
}
