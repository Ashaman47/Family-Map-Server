package Handlers;

import DAO.DataAccessException;
import Service.Clear;
import Result.ClearResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearRequestHandler implements HttpHandler {
    ClearResult f;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Clear clear = new Clear();
                f = clear.clear();

            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (DataAccessException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            f.setSuccess(false);
            f.setMessage(e.getMessage());
        }
        String json = JsonSerializer.serialize(f);
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
        OutputStream respBody = exchange.getResponseBody();
        writeString x = new writeString();
        x.writeString(json, respBody);
        respBody.close();
        exchange.close();
    }
}
