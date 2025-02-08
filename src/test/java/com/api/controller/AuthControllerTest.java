package com.api.controller;

import com.api.handler.StatusCode;
import com.api.modules.user.Role;
import com.api.modules.user.UserDTO;
import com.api.security.auth.*;
import com.api.security.jwt.JwtService;
import com.api.security.jwt.Token;
import com.api.security.jwt.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    AuthService authService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @Test
    void register_validUser_return201() throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", "username@gmail.com", "username1234");

        when(authService.register(Mockito.any())).thenReturn("User created");

        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
                .andExpect(jsonPath("$.message").value("User created"));
    }

    @Test
    void register_usernameNull_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest(null, "username@gmail.com", "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.username").value("Username cant be empty"));
    }

    @Test
    void register_invalidUsername_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest("user", "username@gmail.com", "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.username").value("Username must be 6 - 30 characters"));
    }

    @Test
    void register_emailNull_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", null, "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.email").value("Email cant be empty"));
    }

    @Test
    void register_invalidEmail_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", "username.com", "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.email").value("Invalid email"));
    }

    @Test
    void register_passwordNull_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", "username@gmail.com", null);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.password").value("Password cant be empty"));
    }

    @Test
    void register_invalidPassword_return400() throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", "username@gmail.com", "user12");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.password").value("Password must be greater than 8 characters"));
    }

    @Test
    void login_validUser_returnToken() throws Exception {
        LoginRequest newUser = new LoginRequest("username", "username1234");
        LoginResponse response = new LoginResponse(new Token("faketoken1234"),new UserDTO(UUID.randomUUID().toString(),"username", "username@gmail.com", Role.admin));

        when(authService.login(Mockito.any())).thenReturn(response);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Successful login"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void login_usernameNull_return400() throws Exception {
        LoginRequest newUser = new LoginRequest(null, "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.username").value("Username cant be empty"));
    }

    @Test
    void login_passwordNull_return400() throws Exception {
        LoginRequest newUser = new LoginRequest("username", null);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.data.password").value("Password cant be empty"));
    }
}
