package com.example.mybatis.demo.service;

import com.example.mybatis.demo.model.Address;
import com.example.mybatis.demo.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    private static UserService userService;

    @BeforeClass
    public static void prepare() throws IOException {
        userService = new UserService();
    }

    @Test
    public void findUser() {
        final User user = userService.findUser(1);
        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void addUser() {
        User user = new User("Alice With Score", 21);
        user.setScore(89.839223);

        final User finalUser = userService.addUser(user);
        assertNotNull(finalUser);
        System.out.println(finalUser);
        System.out.println(finalUser.getUserId());
    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setUserName("Jimmy");
        final Integer updates = userService.updateUser(1, user);
        assertNotNull(updates);

        final User updatedUser = userService.findUser(1);
        System.out.println(updatedUser);
        assertNotNull(updatedUser);
        assertNotNull(updatedUser.getScore());
        assertNotNull(updatedUser.getAge());
    }

    @Test
    public void testUpsertUser() {

        User user = new User();
        user.setUserName("Jimmy_upsert_oneoff");
        user.setUserId(1);

        final Integer updates = userService.upsertUser(user);
        assertNotNull(updates);

        final User updatedUser = userService.findUser(1);
        assertNotNull(updatedUser);
        assertEquals(9, updatedUser.getAge().intValue());
        assertNotNull(updatedUser.getScore());

    }

    @Test
    public void testBatchUpsert() {

        User user1 = new User("Jimmyyy", 8);
        user1.setUserId(1);

        User user2 = new User("Cobby", 21);
        user2.setUserId(30);

        final Integer updates = userService.batchUpsert(new ArrayList<User>() {{
            add(user1);
            add(user2);
        }});

        assertNotNull(updates);

        final User user = userService.findUser(1);
        assertNotNull(user.getScore());

    }

    @Test
    public void testFindUserByNameLike() {
        final List<User> users = userService.findUserByNameLike("im");
        assertNotNull(users);
        users.forEach(user -> log.debug(user.toString()));
    }

    @Test
    public void testFindAllAddresses() {
        final List<Address> addresses = userService.findAllAddresses();
        assertNotNull(addresses);
        addresses.forEach(address -> log.debug(address.toString()));
    }

    @Test
    public void testInsertAddress() {
        Address address = new Address();
        address.setCity("Beijing");
        address.setDetail("Chaoyang district");
        final Integer inserts = userService.addAddress(address);
        assertNotNull(inserts);
        assertEquals(1, inserts.intValue());
    }

    @Test
    public void testCustomizedColumns() {
        final List<Map<String, Object>> users = userService.findUsersWithScoreLargerThan(89);
        assertNotNull(users);
        users.forEach(user -> log.debug(user.toString()));
    }

}