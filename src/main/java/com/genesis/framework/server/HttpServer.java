package com.genesis.framework.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.genesis.framework.http.Router;
import com.genesis.framework.service.RouteScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer{
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final Router router = new Router();
    public void registerController(Object controller){
        RouteScanner.scan(controller, router);
    }
    public void start(int port){

        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("Server running on port: {}", port);
            ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
            while(!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, router);
                //handler.run();
                threadPool.execute(handler);
            }
        }catch (IOException e){
            logger.error("Server error: {}", e.getMessage());
        }
    }
}
