package com.sohair.journalApp.service;

import com.sohair.journalApp.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "4, 5, 9",
             "10, 20, 30",
            "100, 200, 300"
    })
    public void sum(int a, int b, int expected){
        assertEquals(expected, a + b);
    }

    @Disabled
    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testWithArgumentsSource(User user) {
        assertNotNull(userService.save(user), "User already exists");
    }
}
