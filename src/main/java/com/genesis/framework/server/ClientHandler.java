package com.genesis.framework.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.Socket;

import com.genesis.framework.http.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesis.framework.http.Request;
import com.genesis.framework.http.Response;

import java.io.InputStreamReader;
import java.io.OutputStream;

public class ClientHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;
    private final Router router;

    public ClientHandler(Socket socket, Router router){
        this.socket = socket;
        this.router = router;
    }
    
    @Override
    public void run(){
        try(
                socket;
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream writer = socket.getOutputStream();
        ){
            Request request = new Request(reader);
            if(!request.isValid()) return;
            Response response = new Response();
            boolean handled = router.dispatch(request, response);
            if(!handled){
                response.setStatusCode(404);
                response.setBody("{\"error\": \"Endpoint not found\"");
                response.setContentType("application/json");
            }
            response.send(writer);
        }catch (Exception e){
            logger.error("Client Error: {}", e.getMessage());
        }
    }


}
