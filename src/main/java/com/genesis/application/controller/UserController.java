package com.genesis.application.controller;

import com.genesis.application.db.Database;
import com.genesis.application.model.User;
import com.genesis.framework.annotations.DELETE;
import com.genesis.framework.annotations.GET;
import com.genesis.framework.annotations.POST;
import com.genesis.framework.annotations.RestController;
import com.genesis.framework.http.Request;
import com.genesis.framework.http.Response;

import java.util.List;

@RestController
public class UserController {
    @GET("/api/users")
    public String listUsers(){
        List<User> users = Database.findAll();
        StringBuilder json = new StringBuilder("[");
        for(int i = 0; i < users.size(); i++){
           User u = users.get(i);
           json.append("{")
                   .append("\"id\":").append(u.getId()).append(",")
                   .append("\"name\":\"").append(u.getName()).append("\",")
                   .append("\"email\":\"").append(u.getEmail()).append("\"")
                   .append("}");
           if(i < users.size() - 1){
               json.append(",");
           }
        }
        json.append("]");
        return json.toString();
    }

    @GET("/api/users/details")
    public String getUser(Request req, Response res){
        String idParam = req.getParam("id");
        if(idParam == null){
            res.setStatusCode(404);
            return "{\"error\": \"ID Required\"}";
        }
        try{
            int id = Integer.parseInt(idParam);
            User user = Database.findById(id);
            if(user == null){
                res.setStatusCode(404);
                return "{\"error\": \"User not Found\"}";
            }
            return String.format(
                    "{\"id\": \"%d\", \"name\": \"%s\", \"email\": \"%s\"}",
                    user.getId(), user.getName(), user.getEmail()
                    );
        }catch (NumberFormatException e){
            res.setStatusCode(400);
            return "{\"error\": \"ID must be a number\"}";
        }
    }
    @POST("/api/users")
    public String createUser(Request req, Response res){
        String body = req.getBody();
        try{
            String name = extractJsonValue(body, "name");
            String email = extractJsonValue(body, "email");
            if(name == null||name.length() < 3){
                res.setStatusCode(400);
                return "{\"error\": \"Invalid name or too short\"}";
            }
            User user = new User(0, name, email);
            User saved = Database.save(user);
            res.setStatusCode(201);

            return String.format(
                    "{\"id\": \"%d\", \"name\": \"%s\", \"email\": \"%s\"}",
                    saved.getId(), saved.getName(), saved.getEmail()
            );
        }catch(Exception e){
            res.setStatusCode(500);
            return "{\"error\": \"Failed to parse JSON\"}";
        }
    }

    @DELETE("/api/users")
    public String deleteUser(Request req, Response res){
        String idParam = req.getParam("id");
        if(idParam == null){
            res.setStatusCode(404);
            return "{\"error\": \"ID Required\"}";
        }
        try{
            int id = Integer.parseInt(idParam);
            boolean deleted = Database.delete(id);
            if(deleted){
                res.setStatusCode(204);
                return "";
            } else{
                res.setStatusCode(404);
                return "{\"error\": \"User not Found\"}";
            }
        }catch (NumberFormatException e){
            res.setStatusCode(400);
            return "{\"error\": \"ID must be a number\"}";
        }
    }
    private String extractJsonValue(String body, String key){
        String search = "\"" + key + "\":";
        int start = body.indexOf(search);
        if(start == -1) return null;
        start += search.length();
        while(body.charAt(start) == ' ' || body.charAt(start)== '"')start++;
        int end = body.indexOf("\"", start);
        return body.substring(start, end);
    }
}
