package com.sohair.journalApp.controller;

import com.sohair.journalApp.model.Journal;
import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.JournalRepository;
import com.sohair.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    JournalService jservice;

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return jservice.save(journal, userName);
    }

    @GetMapping("/journals")
    public ResponseEntity<List<Journal>> getJournal(){
        System.out.println("yep not working");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        List<Journal> journal = jservice.getJournals(userName);
        if(journal != null && !journal.isEmpty()) {
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(journal, HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal journal){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            User user = jservice.getUserByUserName(userName);
            if(user==null){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Optional<Journal> existingJournal = jservice.getJournal(journal.getId());
            if(existingJournal.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Journal journal1 = existingJournal.get();
            if(journal1.getId().equals(user.getId())){
                return new ResponseEntity<>(jservice.update(journal), HttpStatus.FORBIDDEN);
            }
            else{
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable ObjectId id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        jservice.delete(id, userName);
    }
}
