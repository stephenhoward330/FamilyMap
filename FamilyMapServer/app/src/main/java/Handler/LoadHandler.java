package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import Request.LoadRequest;
import Response.Response;
import Service.Services;

import static java.net.HttpURLConnection.HTTP_OK;

public class LoadHandler implements HttpHandler {

    private Services services = new Services();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /load handler");

        // add checks for correct method
        String method = exchange.getRequestMethod();
        System.out.println("method: " + method + " (should be POST)");

        Reader in = new InputStreamReader(exchange.getRequestBody());
        LoadRequest request = gson.fromJson(in, LoadRequest.class);

        Response response = services.load(request);

        exchange.sendResponseHeaders(HTTP_OK, 0);

        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();

        System.out.println("response body: " + gson.toJson(response));
        System.out.println();
    }
}
