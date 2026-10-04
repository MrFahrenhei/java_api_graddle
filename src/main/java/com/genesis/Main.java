package com.genesis;

import com.genesis.application.controller.UserController;
import com.genesis.framework.server.HttpServer;

public class Main{
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        UserController userController = new UserController();
        server.get("/api/users", userController::listUsers);
        server.get("/api/users/detail", userController::getUser);
        server.post("/api/users", userController::createUser);
        server.delete("/api/users", userController::deleteUser);
        server.start(8080);
    }

}
