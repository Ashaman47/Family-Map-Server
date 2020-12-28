package Handlers;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Service.Event;
import Result.EventResult;
import Result.Result;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class EventRequestHandler implements HttpHandler {
    EventResult p = new EventResult();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")){
                Headers reqHeaders = exchange.getRequestHeaders();
                String url = exchange.getRequestURI().getPath();
                url = url.substring(7);
                if (reqHeaders.containsKey("Authorization")){
                    String authToken = reqHeaders.getFirst("Authorization");
                    Database db = new Database();
                    try {
                        Connection conn = db.getConnection();
                        AuthTokenDAO authDAO = new AuthTokenDAO(conn);
                        EventDAO pDAO = new EventDAO(conn);
                        Event Per = new Event();
                        if (pDAO.find(url) != null) {
                            String user = pDAO.find(url).getassociatedUserName();
                            Model.AuthToken a;
                            if (authDAO.find(user) != null) {
                                a = authDAO.find(user);
                            }
                            else{
                                db.closeConnection(false);
                                throw new DataAccessException(user + "Does not exist");
                            }
                            if (a.getAuthToken().equals(authToken)) {
                                db.closeConnection(true);
                                try {
                                    p = Per.event(url);
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                    p.setSuccess(true);
                                } catch (DataAccessException e) {
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                    p = new EventResult();
                                    p.setSuccess(false);
                                    p.setMessage(e.getMessage());
                                }

                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                p.setSuccess(false);
                                p.setMessage("Error: Invalid Auth Token");
                                db.closeConnection(false);
                            }
                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            p = new EventResult();
                            p.setSuccess(false);
                            p.setMessage("Error: Event Doesnt Exist");
                            db.closeConnection(false);
                        }
                    }catch (DataAccessException e){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        p = new EventResult();
                        p.setSuccess(false);
                        p.setMessage("Error: Doesnt Exist");
                    }
                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            }
            else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        if (p.getEventID() == null){
            Result c = new Result();
            c.setMessage(p.getMessage());
            c.setSuccess(p.getSuccess());
            String json = JsonSerializer.serialize(c);
            OutputStream respBody = exchange.getResponseBody();
            writeString x = new writeString();
            x.writeString(json, respBody);
            respBody.close();
            exchange.close();;
        }
        else {
            String json = JsonSerializer.serialize(p);
            OutputStream respBody = exchange.getResponseBody();
            writeString x = new writeString();
            x.writeString(json, respBody);
            respBody.close();
            exchange.close();
        }
    }
}
