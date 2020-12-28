package Handlers;

import DAO.DataAccessException;
import Requests.LoadRequest;
import Service.Load;
import Result.LoadResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class LoadRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }
                LoadRequest L = JsonSerializer.deserialize(buf.toString(),LoadRequest.class);
                Load load = new Load();
                LoadResult f = new LoadResult();
                try {
                   f = load.load(L);
                } catch (DataAccessException e){
                    exchange.close();
                }
                String json = JsonSerializer.serialize(f);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                OutputStream respBody = exchange.getResponseBody();
                writeString x = new writeString();
                x.writeString(json, respBody);
                respBody.close();
            }else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }

}
