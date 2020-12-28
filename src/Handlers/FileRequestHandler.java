package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.io.*;
import java.nio.file.Files;
import java.util.Collections;

public class FileRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath.isEmpty() || (urlPath.charAt(0) == '/' && urlPath.length() == 1)) {
                    urlPath = "/index.html";
                }
                String filePath = "web" + urlPath;
                File f = new File(filePath);
                if (!f.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    urlPath = "/HTML/404.html";
                    filePath = "web" + urlPath;
                    f = new File(filePath);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(f.toPath(), respBody);
                    respBody.close();
                    exchange.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }
}
