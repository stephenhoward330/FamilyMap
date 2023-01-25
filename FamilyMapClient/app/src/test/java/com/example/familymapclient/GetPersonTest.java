package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.PersonMultResponse;
import Tasks.GetPersonTask;
import Tasks.LoginTask;
import Tasks.RegisterTask;

import static org.junit.jupiter.api.Assertions.*;

public class GetPersonTest {
    private RegisterTask registerTask;
    private LoginTask loginTask;
    private GetPersonTask getPersonTask;
    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp() {
        registerTask = new RegisterTask("http://127.0.0.1:8888/user/register");
        loginTask = new LoginTask("http://127.0.0.1:8888/user/login");
        getPersonTask = new GetPersonTask("http://127.0.0.1:8888/person");
        registerRequest = new RegisterRequest("username", "password", "email", "first", "last", "m");
    }

    @Test
    public void getPersonPass() {
        registerTask.doRegister(registerRequest); //in case the server doesn't have this user
        LoginResponse loginResponse = loginTask.doLogin(new LoginRequest("username", "password"));
        PersonMultResponse personMultResponse = getPersonTask.doGetPerson(loginResponse.getAuthToken());

        assertNull(personMultResponse.getMessage());

        Person[] persons = personMultResponse.getData();

        assertEquals("username", persons[0].getAssociatedUsername());

        boolean hasPerson = false;
        for (Person person : persons) {
            if (person.getFirstName().equals("first") && person.getLastName().equals("last")) hasPerson = true;
        }
        assertTrue(hasPerson);
    }

    @Test
    public void getPersonFail() {
        registerTask.doRegister(registerRequest);
        PersonMultResponse personMultResponse = getPersonTask.doGetPerson("wrongAuth");

        assertNull(personMultResponse.getData());
        assertEquals("error: Invalid authToken", personMultResponse.getMessage());
    }
}