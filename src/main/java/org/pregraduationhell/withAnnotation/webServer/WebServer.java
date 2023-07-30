package org.pregraduationhell.withAnnotation.webServer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    private static final int PORT = 8080;

    private WebServer() {

    }

    public static void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Register the routes using the @Controller and @Get annotations
            RouteHandler.registerRoutes();

            for (String route : RouteHandler.getRegisteredRoutes()) {
                server.createContext(route, RouteHandler.findHandler(route));
            }

            server.setExecutor(null); // Use the default executor
            server.start();
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
