package com.illegal.ecommerce.controller;

import com.illegal.ecommerce.domain.model.User;
import com.illegal.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/users")
    public class UserController {

        private final UserService service;

        public UserController(UserService service) {
            this.service = service;
        }

        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody User user) {
            User created = service.createUser(user);
            return ResponseEntity.ok(created);
        }

        @GetMapping
        public ResponseEntity<List<User>> getAllUsers() {
            return ResponseEntity.ok(service.getAllUsers());
        }
    }

