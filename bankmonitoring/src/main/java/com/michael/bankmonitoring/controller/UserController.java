package com.michael.bankmonitoring.controller;

import com.michael.bankmonitoring.model.User;
import com.michael.bankmonitoring.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return repo.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return repo.findAll();
    }
}

