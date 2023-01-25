package com.example.familymapclient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import Model.DataModel;
import Model.Event;
import Model.Person;
import Model.Singleton;
import Proxy.ServerProxy;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.*;

public class LoginFragment extends Fragment {

    private LoginListener loginListener;

    ServerProxy serverProxy;

    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button signInButton;
    private Button registerButton;

    public LoginFragment() {
        // required empty constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof LoginListener)
            loginListener = (LoginListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        serverHostEditText = v.findViewById(R.id.serverHost);
        serverPortEditText = v.findViewById(R.id.serverPort);
        userNameEditText = v.findViewById(R.id.userName);
        passwordEditText = v.findViewById(R.id.password);
        firstNameEditText = v.findViewById(R.id.firstName);
        lastNameEditText = v.findViewById(R.id.lastName);
        emailEditText = v.findViewById(R.id.email);
        maleRadioButton = v.findViewById(R.id.maleButton);
        femaleRadioButton = v.findViewById(R.id.femaleButton);
        signInButton = v.findViewById(R.id.signInButton);
        registerButton = v.findViewById(R.id.registerButton);

        // for testing convenience
        serverHostEditText.setText("10.0.2.2");
        serverPortEditText.setText("8888");
        userNameEditText.setText("sheila");
        // end testing convenience

        signInButton.setEnabled(false);
        registerButton.setEnabled(false);

        maleRadioButton.setChecked(true);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (serverHostEditText.getText().toString().length()>0 && serverPortEditText.getText().toString().length()>0
                        && userNameEditText.getText().toString().length()>0 && passwordEditText.getText().toString().length()>0) {
                    signInButton.setEnabled(true);
                    if (firstNameEditText.getText().toString().length()>0 && lastNameEditText.getText().toString().length()>0
                            && emailEditText.getText().toString().length()>0) {
                        registerButton.setEnabled(true);
                    }
                    else {
                        registerButton.setEnabled(false);
                    }
                }
                else {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }
        };
        serverHostEditText.addTextChangedListener(textWatcher);
        serverPortEditText.addTextChangedListener(textWatcher);
        userNameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return v;
    }

    private void signIn() {
        serverProxy = new ServerProxy(serverHostEditText.getText().toString(), serverPortEditText.getText().toString());
        LoginRequest request = new LoginRequest(userNameEditText.getText().toString(), passwordEditText.getText().toString());
        LoginResponse response = serverProxy.login(request);
        while (response == null) {}

        if (response.getMessage() != null) {
            showToast(response.getMessage());
            return;
        }

        String authToken = response.getAuthToken();
        String rootPersonID = response.getPersonID();

        EventMultResponse eventResponse = serverProxy.event(authToken);
        PersonMultResponse personResponse = serverProxy.person(authToken);
        while (personResponse == null || eventResponse == null) {}

        Event[] events = eventResponse.getData();
        Person[] persons = personResponse.getData();

        DataModel dataModel = Singleton.getInstance();
        dataModel.setAuthToken(authToken);
        dataModel.setRootPersonID(rootPersonID);
        dataModel.setEvents(events);
        dataModel.setPersons(persons);

        loginListener.onLogin();
    }

    private void register() {
        serverProxy = new ServerProxy(serverHostEditText.getText().toString(), serverPortEditText.getText().toString());
        String gender = "";
        if (maleRadioButton.isChecked()) gender = "m";
        else gender = "f";
        RegisterRequest request = new RegisterRequest(userNameEditText.getText().toString(), passwordEditText.getText().toString(),
                emailEditText.getText().toString(), firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                gender);
        RegisterResponse response = serverProxy.register(request);
        while (response == null) {}

        if (response.getMessage() != null) {
            showToast(response.getMessage());
            return;
        }

        String authToken = response.getAuthToken();
        String rootPersonID = response.getPersonID();

        EventMultResponse eventResponse = serverProxy.event(authToken);
        PersonMultResponse personResponse = serverProxy.person(authToken);
        while (personResponse == null || eventResponse == null) {}

        Event[] events = eventResponse.getData();
        Person[] persons = personResponse.getData();

        DataModel dataModel = Singleton.getInstance();
        dataModel.setAuthToken(authToken);
        dataModel.setRootPersonID(rootPersonID);
        dataModel.setEvents(events);
        dataModel.setPersons(persons);

        loginListener.onLogin();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}