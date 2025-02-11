package com.api.controller;

import com.api.handler.StatusCode;
import com.api.modules.user.Role;
import com.api.modules.user.UserDTO;
import com.api.security.auth.*;
import com.api.security.jwt.JwtService;
import com.api.security.jwt.Token;
import com.api.security.jwt.UserDetailsServiceImpl;
import com.api.utils.ResponseMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        when(authService.register(Mockito.any())).thenReturn(new ResponseMessageDTO("User saved successfully"));

        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message").value("User saved successfully"));
    }

    @ParameterizedTest
    @CsvSource({
            ", Username cant be empty",
            "user, Username must be 6 - 30 characters"
    })
    void register_invalidUsername_return400(String username, String expectedMessage) throws Exception {
        RegisterRequest newUser = new RegisterRequest(username, "username@gmail.com", "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.username").value(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({
            ", Email cant be empty",
            "user, Invalid email"
    })
    void register_invalidEmail_return400(String email, String expectedMessage) throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", email, "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.email").value(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({
            ", Password cant be empty",
            "pass, Password must be greater than 8 characters"
    })
    void register_invalidPassword_return400(String password, String expectedMessage) throws Exception {
        RegisterRequest newUser = new RegisterRequest("username", "username@gmail.com", password);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.password").value(expectedMessage));
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
                .andExpect(jsonPath("$.token.jwt").value("faketoken1234"))
                .andExpect(jsonPath("$.user.username").value("username"))
                .andExpect(jsonPath("$.user.email").value("username@gmail.com"))
                .andExpect(jsonPath("$.user.role").value("admin"));
    }

    @Test
    void login_usernameNull_return400() throws Exception {
        LoginRequest newUser = new LoginRequest(null, "username1234");

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.username").value("Username cant be empty"));
    }

    @Test
    void login_passwordNull_return400() throws Exception {
        LoginRequest newUser = new LoginRequest("username", null);

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.password").value("Password cant be empty"));
    }
}
