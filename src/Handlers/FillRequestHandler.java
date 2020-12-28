package Handlers;

import DAO.DataAccessException;
import Service.Fill;
import Result.FillResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")){
                String url = exchange.getRequestURI().getPath();
                url = url.substring(6);
                String gens = "";
                for (int i = 0; i < url.length(); ++i){
                    if (url.charAt(i) == '/'){
                        gens = url.substring(i + 1);
                        url = url.substring(0, i);
                    }
                }
                int gen = 4;
                if (gens.length() != 0) {
                    gen = Integer.parseInt(gens);
                }
                Fill fill = new Fill();
                FillResult f = new FillResult();
                try {
                    f = fill.fill(url, gen);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    f.setSuccess(true);
                }catch (DataAccessException e){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    f.setSuccess(false);
                    f.setMessage("ERROR:" + e.getMessage());
                }
                String json = JsonSerializer.serialize(f);
                OutputStream respBody = exchange.getResponseBody();
                writeString x = new writeString();
                x.writeString(json, respBody);
                respBody.close();
                exchange.close();
            }
            else{
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
