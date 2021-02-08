package com.esgi.services;

import com.esgi.entities.User;
import com.esgi.entities.UserType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserService {
    private static final JSONParser jsonParser = new JSONParser();
    private final String filePath;

    public UserService(String filePath) {
        this.filePath = filePath + "users.json";
    }

    public User login(String login){
        User user = findUser(login);
        if(user == null){
            // we will create this user
            user = new User(UserType.MEMBER, login);
            saveNewUser(user);
        }
        return user;
    }

    private void saveNewUser(User user){
        JSONArray users = getAllUsers();
        try (FileWriter file = new FileWriter(this.filePath)) {
            JSONObject jsonUser = user.toJSONObject();
            users.add(jsonUser);
            file.write(users.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getAllUsers(){
        JSONArray users = new JSONArray();
        try (FileReader reader = new FileReader(this.filePath))
        {
            users = (JSONArray) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User findUser(String login){
        User user = null;
        try (FileReader reader = new FileReader(this.filePath))
        {
            //Read JSON file
            JSONArray users = (JSONArray) jsonParser.parse(reader);
            for (JSONObject userJson : (Iterable<JSONObject>) users) {
                if(userJson.get("login").equals(login)){
                    user = new User(
                            UserType.valueOf((String)userJson.get("type")),
                            (String)userJson.get("login"));
                    break;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return user;
    }
}
