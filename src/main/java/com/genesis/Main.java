package com.genesis;

import com.genesis.application.controller.UserController;
import com.genesis.application.repository.UserRepository;
import com.genesis.application.repository.impl.UserRepositoryImpl;
import com.genesis.application.service.UserService;
import com.genesis.framework.server.HttpServer;

public class Main{
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        UserRepository repository = new UserRepositoryImpl();
        UserService userService = new UserService(repository);
        UserController userController = new UserController(userService);

        server.registerController(userController);
        server.start(8080);
    }

}
