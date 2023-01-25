package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Event;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.EventMultResponse;
import Response.LoginResponse;
import Tasks.GetEventTask;
import Tasks.LoginTask;
import Tasks.RegisterTask;

import static org.junit.jupiter.api.Assertions.*;

public class GetEventTest {
    private RegisterTask registerTask;
    private LoginTask loginTask;
    private GetEventTask getEventTask;
    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp() {
        registerTask = new RegisterTask("http://127.0.0.1:8888/user/register");
        loginTask = new LoginTask("http://127.0.0.1:8888/user/login");
        getEventTask = new GetEventTask("http://127.0.0.1:8888/event");
        registerRequest = new RegisterRequest("username", "password", "email", "first", "last", "m");
    }

    @Test
    public void getEventPass() {
        registerTask.doRegister(registerRequest); //in case the server doesn't have this user
        LoginResponse loginResponse = loginTask.doLogin(new LoginRequest("username", "password"));
        EventMultResponse eventMultResponse = getEventTask.doGetEvent(loginResponse.getAuthToken());

        assertNull(eventMultResponse.getMessage());

        Event[] events = eventMultResponse.getData();

        assertEquals("username", events[0].getAssociatedUsername());
    }

    @Test
    public void getEventFail() {
        registerTask.doRegister(registerRequest);
        EventMultResponse eventMultResponse = getEventTask.doGetEvent("wrongAuth");

        assertNull(eventMultResponse.getData());
        assertEquals("error: Invalid authToken", eventMultResponse.getMessage());
    }
}