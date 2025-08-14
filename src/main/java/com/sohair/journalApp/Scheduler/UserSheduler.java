package com.sohair.journalApp.Scheduler;

import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.UserRepository;
import com.sohair.journalApp.repository.UserRepositoryImpl;
import com.sohair.journalApp.service.EmailService;
import com.sohair.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserSheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

//    @Scheduled(cron = "*/30 * * * * *")
    public void sendEmailToAllUsers() {
        List<User> users = userRepositoryImpl.findAllUsers();
        for (User user : users){
            String allContent = user.getJournalEntries()
                    .stream()
                    .map(Journal::getContent)  // extract content from each journal
                    .collect(Collectors.joining(" "));

            emailService.sendEmail(user.getEmail(), "The minutely schedule", allContent);
        }
    }
}
