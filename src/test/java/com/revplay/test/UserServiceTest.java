package com.revplay.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.UserDAO;
import com.revplay.model.User;
import com.revplay.service.UserService;

public class UserServiceTest {

    private UserDAO userDAOMock;
    private UserService userService;

    @Before
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        userService = new UserService(userDAOMock);
    }

    @Test
    public void testRegisterUserSuccess() {

        when(userDAOMock.registerUser(
                "JUnit User",
                "junit_user@gmail.com",
                "123",
                "USER",
                "pet?",
                "dog"
        )).thenReturn(true);

        boolean result = userService.register(
                "JUnit User",
                "junit_user@gmail.com",
                "123",
                "USER",
                "pet?",
                "dog"
        );

        assertTrue(result);

        verify(userDAOMock).registerUser(
                "JUnit User",
                "junit_user@gmail.com",
                "123",
                "USER",
                "pet?",
                "dog"
        );
    }

    @Test
    public void testRegisterInvalidEmail() {

        boolean result = userService.register(
                "Bad Email",
                "bademail",
                "123",
                "USER",
                "q",
                "a"
        );

        assertFalse(result);

        verify(userDAOMock, never())
                .registerUser(anyString(), anyString(),
                        anyString(), anyString(),
                        anyString(), anyString());
    }

    @Test
    public void testLoginUserSuccess() {

        User mockUser = new User(1, "JUnit User",
                "junit_user@gmail.com", "USER");

        when(userDAOMock.login(
                "junit_user@gmail.com",
                "123"
        )).thenReturn(mockUser);

        User user = userService.login(
                "junit_user@gmail.com",
                "123"
        );

        assertNotNull(user);
        assertEquals("JUnit User", user.getName());

        verify(userDAOMock).login(
                "junit_user@gmail.com",
                "123"
        );
    }

    @Test
    public void testVerifySecurityAnswerWrong() {

        when(userDAOMock.verifySecurityAnswer(
                "junit_user@gmail.com",
                "wrong-answer"
        )).thenReturn(false);

        boolean result = userService.verifySecurityAnswer(
                "junit_user@gmail.com",
                "wrong-answer"
        );

        assertFalse(result);

        verify(userDAOMock).verifySecurityAnswer(
                "junit_user@gmail.com",
                "wrong-answer"
        );
    }

    @After
    public void tearDown() {
        userDAOMock = null;
        userService = null;
    }
}
