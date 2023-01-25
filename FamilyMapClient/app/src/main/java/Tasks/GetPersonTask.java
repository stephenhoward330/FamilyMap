package Tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import Response.PersonMultResponse;

public class GetPersonTask extends AsyncTask<String, Void, PersonMultResponse> {

    private Gson gson;
    private String myUrl;

    public GetPersonTask(String myUrl) {
        this.gson = new Gson();
        this.myUrl = myUrl;
    }

    @Override
    protected PersonMultResponse doInBackground(String... strings) {
        return doGetPerson(strings[0]);
    }

    public PersonMultResponse doGetPerson(String string) {
        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", string);
            connection.connect();

            System.out.println("Connection Response Code: " + connection.getResponseCode());

            Reader in = new InputStreamReader(connection.getInputStream());
            PersonMultResponse response = gson.fromJson(in, PersonMultResponse.class);
            in.close();

            return response;
        }
        catch (Exception e) {
            System.out.println("GetPersonTask error: " + e.getMessage());
        }

        return null;
    }
}