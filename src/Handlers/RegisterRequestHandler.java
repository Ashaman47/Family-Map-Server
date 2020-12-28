package Handlers;
import DAO.DataAccessException;
import Service.Register;
import Result.RegisterResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import Requests.RegisterRequest;

public class RegisterRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RegisterResult f = new RegisterResult();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")){
                InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }
                RegisterRequest r = JsonSerializer.deserialize(buf.toString(),RegisterRequest.class);
                Register reg = new Register();

                try {
                    f = reg.register(r);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    f.setSuccess(true);
                    f.setMessage(null);
                }catch (DataAccessException e){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    f.setSuccess(false);
                    f.setMessage("ERROR: " + e.getMessage());
                }

            }
            else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
            String json = JsonSerializer.serialize(f);
            OutputStream respBody = exchange.getResponseBody();
            writeString x = new writeString();
            x.writeString(json, respBody);
            respBody.close();
            exchange.close();
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }


}
