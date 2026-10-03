package com.genesis;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main{
    public static void main(String[] args) throws Exception{
        System.out.println("Starting server on port 8080... ");
        ServerSocket serverSocket = new ServerSocket(8080);

        while(true){
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            if(bytesRead > 0){
                String request = new String(buffer, 0, bytesRead);
                System.out.println(request);
            }

            String httpResponse = """
HTTP/1.1 200 OK
Content-Type: text/plain

Hellow World!
""";
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }

}
