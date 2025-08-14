package com.sohair.journalApp.Repository;


import com.sohair.journalApp.model.User;
import com.sohair.journalApp.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserRepositoryImplTest {

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testForUserRepositoryImpl() {
        System.out.println(userRepositoryImpl.findAllUsers());
        assertFalse(userRepositoryImpl.findAllUsers().isEmpty());
    }
}
