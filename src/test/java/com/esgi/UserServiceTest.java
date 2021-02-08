package com.esgi;

import com.esgi.entities.User;
import com.esgi.entities.UserType;
import com.esgi.services.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class UserServiceTest {
    UserService userService = new UserService("src/test/java/com/esgi/data/");

    @Test
    public void loginTest(){
        LocalDate now = LocalDate.now();
        String login = "testLogin" + now;
        User futurUser = userService.login(login);
        Assert.assertEquals(futurUser.getLogin(), login);
        Assert.assertEquals(futurUser.getType(), UserType.MEMBER);
    }

    @AfterClass
    public static void clearAll(){
        try (FileWriter file = new FileWriter("src/test/java/com/esgi/data/users.json")) {
            JSONArray initial = new JSONArray();
            User initialUser = new User(UserType.MEMBER, "john");
            initial.add(initialUser.toJSONObject());
            file.write(initial.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
