package com.dailyExpenses.Daily.Expenses.Sharing.Application.controller;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importing all necessary annotations
import com.dailyExpenses.Daily.Expenses.Sharing.Application.service.UserService;


@RestController
//http://localhost:8082/api/users
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

  // post  http://localhost:8082/api/users
    @PostMapping
    public ResponseEntity<UserData> createUser(@RequestBody UserData userData) {
        UserData createdUser = userService.createUser(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
  // GET http://localhost:8082/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable Long id) {
        UserData user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
