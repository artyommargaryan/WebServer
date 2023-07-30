package org.pregraduationhell.withoutAnnotation.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler implements HttpHandler {
    private static final String HTML_PATH = "./src/main/resources/views/";
    private final String fileName;

    public Handler(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = readHtmlFile();
        sendResponse(httpExchange, response);
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String readHtmlFile() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(HTML_PATH, fileName));
        return new String(encoded);
    }
}
