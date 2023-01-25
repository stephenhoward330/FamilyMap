package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Tasks.LoginTask;
import Tasks.RegisterTask;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private RegisterTask registerTask;
    private LoginTask loginTask;
    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp() {
        registerTask = new RegisterTask("http://127.0.0.1:8888/user/register");
        loginTask = new LoginTask("http://127.0.0.1:8888/user/login");
        registerRequest = new RegisterRequest("username", "password", "email", "first", "last", "m");
    }

    @Test
    public void loginPass() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        registerTask.doRegister(registerRequest);
        LoginResponse loginResponse = loginTask.doLogin(loginRequest);

        assertNull(loginResponse.getMessage());
        assertEquals("username", loginResponse.getUserName());
    }

    @Test
    public void loginFail() {
        registerTask.doRegister(registerRequest);

        LoginRequest loginRequest = new LoginRequest("username", "pword");
        LoginResponse loginResponse = loginTask.doLogin(loginRequest);

        assertNull(loginResponse.getUserName());
        assertEquals("error: incorrect password", loginResponse.getMessage());

        loginRequest = new LoginRequest("uname", "password");
        loginResponse = loginTask.doLogin(loginRequest);

        assertNull(loginResponse.getUserName());
        assertEquals("error: incorrect username", loginResponse.getMessage());
    }
}