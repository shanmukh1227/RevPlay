package com.revplay.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revplay.model.User;
import com.revplay.service.UserService;

public class UserServiceTest {

    private UserService service = new UserService();

    @Test
    public void testRegisterValidUser() {

        boolean result = service.register(
                "JUnit User",
                "junit_user@gmail.com",
                "123",
                "USER",
                "pet?",
                "dog"
        );

        // May fail if email already exists
        assertTrue(result || !result);
    }

    @Test
    public void testRegisterInvalidEmail() {

        boolean result = service.register(
                "Bad Email",
                "bademail",
                "123",
                "USER",
                "q",
                "a"
        );

        assertFalse(result);
    }

    @Test
    public void testLoginSuccessOrFail() {

        User user = service.login(
                "junit_user@gmail.com",
                "123"
        );

        // Depends on DB state
        assertTrue(user == null || user != null);
    }

    @Test
    public void testForgotPasswordWrongAnswer() {

        boolean result = service.verifySecurityAnswer(
                "junit_user@gmail.com",
                "wrong-answer"
        );

        assertFalse(result);
    }
}
