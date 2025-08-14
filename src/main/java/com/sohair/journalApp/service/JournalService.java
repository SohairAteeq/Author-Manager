package com.sohair.journalApp.service;

import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.JournalRepository;
import com.sohair.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JournalRepository jrepo;

    @Transactional
    public ResponseEntity<Journal> save(Journal journal, String userName) {
        User user = userService.getUserByUserName(userName);
        if (user != null) {
            journal.setDate(LocalDateTime.now());
            jrepo.save(journal); // save journal first
            user.getJournalEntries().add(journal);
            userService.updateWithoutPasswordHash(user); // new helper method
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public Journal update(Journal journal) {
        Journal existingJournal = jrepo.findById(journal.getId())
                .orElseThrow(() -> new RuntimeException("Journal not found with id: " + journal.getId()));
        existingJournal.setTitle(journal.getTitle());
        existingJournal.setContent(journal.getContent());
        return jrepo.save(existingJournal);
    }

    public Optional<Journal> getJournal(ObjectId id){
        return jrepo.findById(id);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<Journal> getJournals(String userName) {
        return userService.findByUserName(userName).getJournalEntries();
    }

    public void delete(ObjectId id, String userName){
        jrepo.deleteById(id);
        userService.findByUserName(userName)
                .getJournalEntries()
                .removeIf(journal -> journal.getId().equals(id));
        userService.save(userService.findByUserName(userName));
    }
}
