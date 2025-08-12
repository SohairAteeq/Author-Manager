package com.sohair.journalApp.service;

import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if(user.getUserName() ==null){
            System.out.println("User name is null");
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("ADMIN"));
        }
        return userRepository.save(user);
    }
}
