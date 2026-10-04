package com.genesis.application.controller;

import com.genesis.application.db.Database;
import com.genesis.application.model.User;
import com.genesis.framework.http.Request;
import com.genesis.framework.http.Response;

import java.util.List;

public class UserController {
    public void listUsers(Request req, Response res){
        List<User> users = Database.findAll();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++){
            User u = users.get(i);
            json.append(String.format("{\"id\":%d, \"name\":\"%s\", \"email\":\"%s\"}",
                   u.getId(), u.getName(), u.getEmail()
            ));
            if( i < users.size() - 1) json.append(",");
        }
        json.append("]");
        res.setBody(json.toString());
        res.setStatusCode(200);
        res.setContentType("application/json");
    }
}
