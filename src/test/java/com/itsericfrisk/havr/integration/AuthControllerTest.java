package com.itsericfrisk.havr.integration;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ADMIN_EMAIL = "admin@email.se";
    private static final String ADMIN_PASS = "password";

    private static final String USER_EMAIL = "user@email.se";
    private static final String USER_PASS = "password";

    @Test
    @DisplayName("Register user, login and fetch categories")
    void registerUser_login_fetchCategories() throws Exception {
        Cookie adminCookie = login(ADMIN_EMAIL, ADMIN_PASS);
        registerUser(adminCookie);
        Cookie userCookie = login(USER_EMAIL, USER_PASS);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(userCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    private void registerUser(Cookie adminCookie) throws Exception {
        String body = """
                {"email": "email@email.com", "name": "user", "password": "password123"}
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .cookie(adminCookie))
                .andExpect(status().isCreated());
    }

    private Cookie login(String email, String password) throws Exception {
        String body = """
                {"email": "%s", "password": "%s"}
                """.formatted(email, password);

        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getCookie("jwt");
    }
}
