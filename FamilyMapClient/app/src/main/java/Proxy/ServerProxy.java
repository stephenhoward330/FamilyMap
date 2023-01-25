package Proxy;

import java.util.concurrent.ExecutionException;

import Request.*;
import Response.*;
import Tasks.GetEventTask;
import Tasks.GetPersonTask;
import Tasks.LoginTask;
import Tasks.RegisterTask;

public class ServerProxy {
    private String serverHost;
    private String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public RegisterResponse register(RegisterRequest request) {
        String myUrl = "http://" + serverHost + ":" + serverPort + "/user/register";
        RegisterTask registerTask = new RegisterTask(myUrl);
        RegisterResponse response = null;
        try {
            response = registerTask.execute(request).get();
        } catch (ExecutionException e) {
            System.out.println("register execution exception: " + e.getMessage());
        } catch (InterruptedException i) {
            System.out.println("register interruption exception: " + i.getMessage());
        }
        return response;
    }

    public LoginResponse login(LoginRequest request) {
        String myUrl = "http://" + serverHost + ":" + serverPort + "/user/login";
        LoginTask loginTask = new LoginTask(myUrl);
        LoginResponse response = null;
        try {
            response = loginTask.execute(request).get();
        } catch (ExecutionException e) {
            System.out.println("login execution exception: " + e.getMessage());
        } catch (InterruptedException i) {
            System.out.println("login interruption exception: " + i.getMessage());
        }
        return response;
    }

    public PersonMultResponse person(String authToken) {
        String myUrl = "http://" + serverHost + ":" + serverPort + "/person";
        GetPersonTask getPersonTask = new GetPersonTask(myUrl);
        PersonMultResponse response = null;
        try {
            response = getPersonTask.execute(authToken).get();
        } catch (ExecutionException e) {
            System.out.println("person execution exception: " + e.getMessage());
        } catch (InterruptedException i) {
            System.out.println("person interruption exception: " + i.getMessage());
        }
        return response;
    }

    public EventMultResponse event(String authToken) {
        String myUrl = "http://" + serverHost + ":" + serverPort + "/event";
        GetEventTask getEventTask = new GetEventTask(myUrl);
        EventMultResponse response = null;
        try {
            response = getEventTask.execute(authToken).get();
        } catch (ExecutionException e) {
            System.out.println("event execution exception: " + e.getMessage());
        } catch (InterruptedException i) {
            System.out.println("event interruption exception: " + i.getMessage());
        }
        return response;
    }
}