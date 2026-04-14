package com.example.demochatgpt.integration;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 🔥 desactiva seguridad
@ActiveProfiles("test")
@Transactional // 🔥 rollback después de cada test
class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllRoles() throws Exception {

        mockMvc.perform(get("/api/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                // comprobamos que existen los roles iniciales
                .andExpect(jsonPath("$[?(@.name=='USER')]").exists())
                .andExpect(jsonPath("$[?(@.name=='ADMIN')]").exists());
    }

    @Test
    void shouldCreateRole() throws Exception {

        String json = """
            {
                "name": "TEST_ROLE"
            }
            """;

        mockMvc.perform(post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void shouldCreateRoleAndThenAppearInList() throws Exception {

        String json = """
            {
                "name": "NEW_ROLE"
            }
            """;

        // crear rol
        mockMvc.perform(post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // comprobar que existe
        mockMvc.perform(get("/api/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name=='NEW_ROLE')]").exists());
    }


}