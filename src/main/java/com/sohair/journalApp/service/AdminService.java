package com.sohair.journalApp.service;

import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.JournalRepository;
import com.sohair.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JournalRepository journalRepository;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + userName);
        }
        for (Journal journal : user.getJournalEntries()) {
            if (journal != null && journal.getId() != null) {
                journalRepository.deleteById(journal.getId());
            }
        }
        userRepository.delete(user);
    }
}
