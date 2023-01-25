package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Response.Response;
import Service.Services;

import static java.net.HttpURLConnection.HTTP_OK;

public class ClearHandler implements HttpHandler {

    private Services services = new Services();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /clear handler");

        // add checks for correct method
        String method = exchange.getRequestMethod();
        System.out.println("method: " + method + " (should be POST)");

        Response response = services.clear();

        exchange.sendResponseHeaders(HTTP_OK, 0);

        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();

        System.out.println("response body: " + gson.toJson(response));
        System.out.println();
    }
}
