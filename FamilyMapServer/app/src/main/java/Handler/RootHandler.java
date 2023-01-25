package Handler;

import android.annotation.TargetApi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

public class RootHandler implements HttpHandler {

    @TargetApi(26)
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: / handler");

        // add checks for correct method
        String method = exchange.getRequestMethod();
        System.out.println("method: " + method + " (should be GET)");

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("Path: " + path);

        String rootPath = "C:\\Users\\steph\\AndroidStudioProjects\\FamilyMapServer\\app\\web";
        System.out.println("Complete path: " + rootPath + path);
        System.out.println();

        if (path.equals("/")) {
            path = "/index.html";
        }

        File returnFile = new File(rootPath + path);
        if (returnFile.exists() && returnFile.canRead()) {
            exchange.sendResponseHeaders(HTTP_OK, 0);
            Path returnPath = Paths.get(returnFile.getPath());
            Files.copy(returnPath, exchange.getResponseBody());
        }
        else {
            exchange.sendResponseHeaders(HTTP_NOT_FOUND, -1);
        }

        exchange.getResponseBody().close();
    }
}
