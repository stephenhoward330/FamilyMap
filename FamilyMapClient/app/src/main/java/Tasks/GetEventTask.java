package Tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import Response.EventMultResponse;

public class GetEventTask extends AsyncTask<String, Void, EventMultResponse> {

    private Gson gson;
    private String myUrl;

    public GetEventTask(String myUrl) {
        this.gson = new Gson();
        this.myUrl = myUrl;
    }

    @Override
    protected EventMultResponse doInBackground(String... strings) {
        return doGetEvent(strings[0]);
    }

    public EventMultResponse doGetEvent(String string) {
        try {

            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", string);
            connection.connect();

            System.out.println("Connection Response Code: " + connection.getResponseCode());

            Reader in = new InputStreamReader(connection.getInputStream());
            EventMultResponse response = gson.fromJson(in, EventMultResponse.class);
            in.close();

            return response;
        }
        catch (Exception e) {
            System.out.println("GetEventTask error: " + e.getMessage());
        }

        return null;
    }

}