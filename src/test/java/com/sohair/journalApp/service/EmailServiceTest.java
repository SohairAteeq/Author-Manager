package com.sohair.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void testEmailService() {
        emailService.sendEmail("atombomb893@gmail.com", "Test succeeded from journal app", "This is a test email from journal app");
        assertTrue(true);
    }
}
