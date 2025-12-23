package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Spring Boot backend is running successfully!";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Anurag", "anurag@example.com"));
        users.add(new User(2L, "Rahul", "rahul@example.com"));
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // In real apps, save to DB
        user.setId(100L);
        return user;
    }
}
