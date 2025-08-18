package com.sohair.journalApp.controller;

import com.sohair.journalApp.cache.AppCache;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.service.AdminService;
import com.sohair.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AppCache appCache;

    @Autowired
    AdminService adminService;

    @GetMapping
    public List<User> getAllUsers() {
        // Logic to retrieve all users
        return adminService.getAllUsers();
    }

    @DeleteMapping
    public void delete(@RequestBody User user){
        adminService.delete(user.getUserName());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody  User user) {
        try{
            User createdUser = adminService.createUser(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Bad Request
        }
    }

    @GetMapping("clear-app-cache")
    public ResponseEntity<?> clearAppCache() {
        appCache.init();
        return ResponseEntity.ok("App cache cleared successfully.");
    }
}
