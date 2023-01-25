package com.example.familymapclient;

import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.RegisterRequest;
import Response.RegisterResponse;
import Tasks.RegisterTask;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private RegisterTask registerTask;
    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp() {
        registerTask = new RegisterTask("http://127.0.0.1:8888/user/register");
        registerRequest = new RegisterRequest("username", "password", "email", "first", "last", "m");
    }

    @Test
    public void registerPass() {
        clear();
        RegisterResponse registerResponse = registerTask.doRegister(registerRequest);

        assertNull(registerResponse.getMessage());
        assertEquals("username", registerResponse.getUserName());
    }

    @Test
    public void registerFail() {
        clear();
        registerTask.doRegister(registerRequest);
        RegisterResponse registerResponse = registerTask.doRegister(registerRequest);

        assertNull(registerResponse.getUserName());
        assertEquals("error: username taken", registerResponse.getMessage());
    }

    private void clear() {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://127.0.0.1:8888/clear");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            Reader in = new InputStreamReader(connection.getInputStream());
            RegisterResponse response = gson.fromJson(in, RegisterResponse.class);
            in.close();
        }
        catch (Exception e) {
            System.out.println("RegisterTest error: " + e.getMessage());
        }
        assertTrue(true);
    }
}