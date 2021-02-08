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
    private static final String filePath = "./src/main/java/com/esgi/data/users.json";

    public static User login(String login){
        User user = findUser(login);
        if(user == null){
            // we will create this user
            user = new User(UserType.MEMBER, login);
            saveNewUser(user);
        }
        return user;
    }

    private static void saveNewUser(User user){
        JSONArray users = getAllUsers();
        try (FileWriter file = new FileWriter(filePath)) {
            JSONObject jsonUser = user.toJSONObject();
            users.add(jsonUser);
            file.write(users.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getAllUsers(){
        JSONArray users = new JSONArray();
        try (FileReader reader = new FileReader(filePath))
        {
            users = (JSONArray) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static User findUser(String login){
        User user = null;
        try (FileReader reader = new FileReader(filePath))
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
