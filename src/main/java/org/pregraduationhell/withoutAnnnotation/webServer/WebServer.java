package org.pregraduationhell.withoutAnnnotation.webServer;

import com.sun.net.httpserver.HttpServer;
import org.pregraduationhell.withoutAnnnotation.hadlers.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    private static final int PORT = 8080;

    public static void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Define handlers for different contexts
            server.createContext("/", new Handler("folder1/index.html"));
            server.createContext("/page2", new Handler("folder2/index.html"));
    
            server.setExecutor(null); // Use the default executor

            server.start();
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
