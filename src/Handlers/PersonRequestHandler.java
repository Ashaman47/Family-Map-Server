package Handlers;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Service.Person;
import Result.PersonResult;
import Result.Result;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class PersonRequestHandler implements HttpHandler {
    PersonResult p = new PersonResult();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")){
                Headers reqHeaders = exchange.getRequestHeaders();
                String url = exchange.getRequestURI().getPath();
                url = url.substring(8);
                if (reqHeaders.containsKey("Authorization")){
                    String authToken = reqHeaders.getFirst("Authorization");
                    Database db = new Database();
                    try {
                        Connection conn = db.getConnection();
                        AuthTokenDAO authDAO = new AuthTokenDAO(conn);
                        PersonDAO pDAO = new PersonDAO(conn);
                        Person Per = new Person();
                        if (pDAO.find(url) != null) {
                            String user = pDAO.find(url).getAssociatedUsername();
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
                                    p = Per.person(url);
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                    p.setSuccess(true);
                                } catch (DataAccessException e) {
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                    p.setSuccess(false);
                                    p.setMessage("Error: Unable to find person");
                                }
                                String json = JsonSerializer.serialize(p);
                                OutputStream respBody = exchange.getResponseBody();
                                writeString x = new writeString();
                                x.writeString(json, respBody);
                                respBody.close();
                                exchange.close();
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                p.setSuccess(false);
                                p.setMessage("Error: Invalid Auth Token");
                                db.closeConnection(false);
                            }
                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            p.setSuccess(false);
                            p.setMessage("Error: Person Doesnt Exist");
                            db.closeConnection(false);
                        }
                    }catch (DataAccessException e){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        p = new PersonResult();
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
        if (p.getPersonID() == null){
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
