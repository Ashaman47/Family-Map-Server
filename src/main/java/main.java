package main.java;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import Handlers.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class main {
    public static void main(String[] args) throws IOException {
        startServer(Integer.parseInt(args[0]));
    }
    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/fill/", new FillRequestHandler());
        server.createContext("/person", new PeopleRequestHandler());
        server.createContext("/person/", new PersonRequestHandler());
        server.createContext("/event",new EventsRequestHandler());
        server.createContext("/event/",new EventRequestHandler());
    }
}
