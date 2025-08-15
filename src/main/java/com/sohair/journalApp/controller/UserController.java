package com.sohair.journalApp.controller;

import com.sohair.journalApp.api.response.WeatherResponse;
import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.service.UserService;
import com.sohair.journalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.getUser(userName);
        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(user, HttpStatus.BAD_GATEWAY);
    }

    @DeleteMapping
    public void delete(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        userService.delete(userName);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User existingUser = userService.getUserByUserName(userName);
        if(existingUser != null) {
            existingUser.setPassword(user.getPassword());
            existingUser.setUserName(user.getUserName());
            return new ResponseEntity<>(userService.save(existingUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/greeting/{city}")
    public ResponseEntity<?> greeting(@PathVariable String city) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greeting = "";
        if(weatherResponse != null){
            return new ResponseEntity<>("Hello " + auth.getName() + "! The weather in " + city + " is " + weatherResponse.getCurrent().getTemperature(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Hello " + auth.getName() + "! Weather data is not available at the moment.", HttpStatus.OK);
        }
    }
}
