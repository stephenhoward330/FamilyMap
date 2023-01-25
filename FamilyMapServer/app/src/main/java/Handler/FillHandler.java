package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

import Response.Response;
import Service.Services;

import static java.net.HttpURLConnection.HTTP_OK;

public class FillHandler implements HttpHandler {

    private Services services = new Services();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /fill handler");

        // add checks for correct method
        String method = exchange.getRequestMethod();
        System.out.println("method: " + method + " (should be POST)");

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("Path: " + path);
        String username = path.substring(6);
        int numGens = 0;
        if (username.indexOf('/') != -1) {
            path = username.substring(username.indexOf('/'));
            username = username.substring(0, username.indexOf('/'));
        }
        else {
            path = "";
        }

        if (path.length() > 1) {
            numGens = Integer.parseInt(path.substring(1));
        }

        Response response = services.fill(username, numGens);

        exchange.sendResponseHeaders(HTTP_OK, 0);

        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();

        System.out.println("response body: " + gson.toJson(response));
        System.out.println();
    }
}
