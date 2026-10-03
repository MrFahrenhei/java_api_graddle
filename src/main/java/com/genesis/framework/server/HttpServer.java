package com.genesis.framework.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer{
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("Server running on port: {}", port);
            while(!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.run();
            }
        }catch (IOException e){
            logger.error("Server error: {}", e.getMessage());
        }
    }
}
