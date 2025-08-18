package com.sohair.journalApp.service;

import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.JournalRepository;
import com.sohair.journalApp.repository.UserRepository;
import com.sohair.journalApp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalRepository journalRepository;

    public User save(User user) {
        if (!user.getPassword().startsWith("$2")) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
        }
        if(userRepository.findAll().contains(user)){
            return null;
        }
        else{
            return userRepository.save(user); // save user with password hash
        }
    }

    public User getUser(String userName) {
        log.info("Finding user by username: {}", userName);
        return userRepository.findByUserName(userName);
    }

    public void delete(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + userName);
        }
        if(!user.getRoles().contains("ADMIN")){
            throw new RuntimeException("User not permitted to do that");
        }
        for (Journal journal : user.getJournalEntries()) {
            if (journal != null && journal.getId() != null) {
                journalRepository.deleteById(journal.getId());
            }
        }
        userRepository.delete(user); // pass entity instead of custom delete method
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public User updateWithoutPasswordHash(User user) {
        return userRepository.save(user); // no password touch
    }

    public String verify(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(), user.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            // Extract role as String
            String role = authentication.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority(); // e.g., "ROLE_WORKER"

            return jwtUtil.generateToken(user.getUserName(), role);
        } else {
            return "Login failed";
        }
    }

}
