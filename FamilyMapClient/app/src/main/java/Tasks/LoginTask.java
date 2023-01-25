package Tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.LoginRequest;
import Response.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

    private Gson gson;
    private String myUrl;

    public LoginTask(String myUrl) {
        this.gson = new Gson();
        this.myUrl = myUrl;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... requests) {
        return doLogin(requests[0]);
    }

    public LoginResponse doLogin(LoginRequest request) {
        try {

            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            Writer out = new OutputStreamWriter(connection.getOutputStream());
            gson.toJson(request, out);
            out.close();

            System.out.println("request body: " + gson.toJson(request));

            System.out.println("Connection Response Code: " + connection.getResponseCode());

            Reader in = new InputStreamReader(connection.getInputStream());
            LoginResponse response = gson.fromJson(in, LoginResponse.class);
            in.close();

            return response;
        }
        catch (Exception e) {
            System.out.println("LoginTask error: " + e.getMessage());
        }

        return null;
    }

}