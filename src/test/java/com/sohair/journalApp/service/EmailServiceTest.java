package com.sohair.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void testEmailService() {
        emailService.sendEmail("atombomb893@gmail.com", "Test succeeded from journal app", "This is a test email from journal app");
    }
}
