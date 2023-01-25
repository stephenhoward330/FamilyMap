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

public class EventHandler implements HttpHandler {

    private Services services = new Services();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /event handler");

        // add checks for correct method and authorization
        String method = exchange.getRequestMethod();
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        System.out.println("method: " + method + " (should be GET)");

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("Path: " + path);

        if (!path.contains("/event/")) { // no eventID, return all events
            Response response = services.event(auth);

            exchange.sendResponseHeaders(HTTP_OK, 0);

            Writer out = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(response, out);
            out.close();

            System.out.println("response body: " + gson.toJson(response));
            System.out.println();
        }
        else { // eventID, return one event
            String eventID = path.substring(7); // to remove the /event/

            Response response = services.event(eventID, auth);

            exchange.sendResponseHeaders(HTTP_OK, 0);

            Writer out = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(response, out);
            out.close();

            System.out.println("response body: " + gson.toJson(response));
            System.out.println();
        }
    }
}
