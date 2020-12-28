package Handlers;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Service.Events;
import Result.EventsResult;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class EventsRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventsResult p = new EventsResult();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")){
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")){
                    String authToken = reqHeaders.getFirst("Authorization");
                    Database db = new Database();
                    try {
                        Connection conn = db.getConnection();
                        AuthTokenDAO authDAO = new AuthTokenDAO(conn);
                        if (authDAO.findAuth(authToken) != null) {
                            String user = authDAO.findAuth(authToken).getUserName();
                            db.closeConnection(true);
                            Events Per = new Events();
                            try {
                                p = Per.events(user);
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                p.setSuccess(true);
                            } catch(DataAccessException e){
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                p.setSuccess(false);
                                p.setMessage("error" + e.getMessage());
                            }
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            p.setSuccess(false);
                            p.setMessage("error: Invalid AuthToken");
                            db.closeConnection(false);
                        }
                    }catch (DataAccessException e){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

                    }
                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            }
            else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            String json = JsonSerializer.serialize(p);
            OutputStream respBody = exchange.getResponseBody();
            writeString x = new writeString();
            x.writeString(json, respBody);
            respBody.close();

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }
}
