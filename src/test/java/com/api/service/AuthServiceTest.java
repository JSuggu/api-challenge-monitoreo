package com.api.service;

import com.api.modules.user.Role;
import com.api.modules.user.User;
import com.api.modules.user.UserDTO;
import com.api.modules.user.UserRepository;
import com.api.security.auth.AuthService;
import com.api.security.auth.LoginRequest;
import com.api.security.auth.LoginResponse;
import com.api.security.auth.RegisterRequest;
import com.api.utils.ResponseMessageDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthService authService;

    @BeforeAll
    void setUp(){
        RegisterRequest registerData = new RegisterRequest("username", "username@gmail.com", "username1234");
        authService.register(registerData);
    }
    @AfterAll
    void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    void register_savedSuccessful_returnUser() {
        RegisterRequest registerData = new RegisterRequest("user", "user@gmail.com", "username1234");
        ResponseMessageDTO response = authService.register(registerData);
        User savedUser = userRepository.findByUsername("user").orElseThrow();

        assertEquals("User saved successfully", response.getMessage());
        assertTrue(passwordEncoder.matches("username1234",savedUser.getPassword()));
    }

    @Test
    void register_userAlreadyExist_throwException() throws DataIntegrityViolationException{
        RegisterRequest registerData2 = new RegisterRequest("username", "user2@gmail.com", "password1234");
        assertThrows(DataIntegrityViolationException.class, () -> authService.register(registerData2));

        RegisterRequest registerData3 = new RegisterRequest("newUsername", "username@gmail.com", "password1234");
        assertThrows(DataIntegrityViolationException.class, () -> authService.register(registerData3));
    }

    @Test
    void login_validUser_returnTokenAndUser(){
        LoginRequest loginData = new LoginRequest("username", "username1234");
        LoginResponse response = authService.login(loginData);

        assertEquals(String.class, response.getToken().jwt().getClass());
        assertEquals(UserDTO.class, response.getUser().getClass());
        assertEquals(Role.admin, response.getUser().getRole());
    }

    @Test
    void login_invalidUser_throwException() throws BadCredentialsException{
        LoginRequest loginData = new LoginRequest("us", "username1234");
        LoginRequest loginData2 = new LoginRequest("username", "wrongPassword");

        assertThrows(BadCredentialsException.class, () -> authService.login(loginData));
        assertThrows(BadCredentialsException.class, () -> authService.login(loginData2));
    }

}
