package Tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.RegisterRequest;
import Response.*;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private Gson gson;
    private String myUrl;

    public RegisterTask(String myUrl) {
        this.gson = new Gson();
        this.myUrl = myUrl;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... requests) {
        return doRegister(requests[0]);
    }

    public RegisterResponse doRegister(RegisterRequest request) {
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
            RegisterResponse response = gson.fromJson(in, RegisterResponse.class);
            in.close();

            return response;
        }
        catch (Exception e) {
            System.out.println("RegisterTask error: " + e.getMessage());
        }

        return null;
    }
}