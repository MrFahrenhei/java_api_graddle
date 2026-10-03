package com.genesis.framework.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesis.framework.http.Request;
import com.genesis.framework.http.Response;

import java.io.InputStreamReader;
import java.io.OutputStream;

public class ClientHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run(){
        try(socket;
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
OutputStream writer = socket.getOutputStream();
        ){
            Request request = new Request(reader);
            if(!request.isValid()) return;
            Response response = new Response();
            String path = request.getPath();
            if(path.equals("/")){
                path = "/index.html";
            }
            InputStream fileStream = getClass().getResourceAsStream(path);
            if(fileStream!=null){
                byte[] fileBytes = fileStream.readAllBytes();
                response.setStatusCode(200);
                response.setContentType(guessContentType(path));
                response.setBody(fileBytes);
            }else {
                response.setStatusCode(400);
                response.setContentType("text/html");
                response.setBody("<h1>404 - File Not Found</h1>");
            }
            response.send(writer);
        }catch (Exception e){
            logger.error("Client Error: {}", e.getMessage());
        }
        
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
}
