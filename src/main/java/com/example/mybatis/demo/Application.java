package com.example.mybatis.demo;

import com.example.mybatis.demo.model.User;
import com.example.mybatis.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {

        UserService userService = new UserService();
        final User user = userService.findUser(1);

        log.info("Find user with id 1...");
        System.out.println(user);

    }

}
