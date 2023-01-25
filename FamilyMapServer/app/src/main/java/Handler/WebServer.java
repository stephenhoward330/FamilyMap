package Handler;

import java.net.*;
import com.sun.net.httpserver.*;

import Dao.Database;

public class WebServer {

    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer();
        Database db = new Database();
        db.createTables();

        webServer.startServer();
    }

    private void startServer() throws Exception {

        int port = 8889;

        System.out.println("server listening on port: " + port);
        System.out.println();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());

        server.start();
    }

}
