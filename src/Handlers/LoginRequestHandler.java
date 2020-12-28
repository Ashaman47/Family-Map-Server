package Handlers;

import DAO.DataAccessException;
import Requests.LoginRequest;
import Service.Login;
import Result.LoginResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class LoginRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }
                LoginRequest L = JsonSerializer.deserialize(buf.toString(), LoginRequest.class);
                Login login = new Login();
                LoginResult f = new LoginResult();
                try{
                   f = login.login(L);
                   exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                }catch (DataAccessException e){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    f.setSuccess(false);
                    f.setMessage("ERROR: " + e.getMessage());
                }
                String json = JsonSerializer.serialize(f);
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
