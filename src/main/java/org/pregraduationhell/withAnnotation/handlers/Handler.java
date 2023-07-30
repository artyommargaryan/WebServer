package org.pregraduationhell.withAnnotation.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler implements MethodHandler {
    private static final String HTML_PATH = "./src/main/resources/views/";
    private final String fileName;

    public Handler(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String handle() {
        try {
            return readHtmlFile();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading HTML file.";
        }
    }

    private String readHtmlFile() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(HTML_PATH, fileName));
        return new String(encoded);
    }
}
