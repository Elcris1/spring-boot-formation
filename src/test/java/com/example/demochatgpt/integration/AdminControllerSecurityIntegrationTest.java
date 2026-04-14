package com.example.demochatgpt.integration;


import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;
import com.example.demochatgpt.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils; // tu clase

    @Autowired
    private UserRepository userRepository;

    @Test
    void adminController_shouldAllowAccessWithValidToken() throws Exception {

        // 🔹 obtener usuario real de H2 (el admin del DataInitializer)
        User admin = userRepository.findByEmail("admin@email.com")
                .orElseThrow();

        // 🔹 generar token
        String token = jwtUtils.generateToken(admin);

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("HELLO FROM ADMIN PANEL"));
    }

    @Test
    void adminController_shouldDenyAccess_whenNoToken() throws Exception {

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden()); // o 401 según config

    }

    @Test
    void adminController_shouldDenyAccess_whenTokenFromUserRole() throws Exception {
        User user = userRepository.findByEmail("user@email.com")
                .orElseThrow();

        // 🔹 generar token
        String token = jwtUtils.generateToken(user);

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}