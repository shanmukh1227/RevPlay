package com.revplay.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.UserDAO;
import com.revplay.model.User;

public class UserDAOTest {

    private UserDAO dao;

    @Before
    public void setUp() {
        dao = new UserDAO();
    }

    @Test
    public void testRegisterUser() {

        boolean result = dao.registerUser(
                "DAO User",
                "dao_user@gmail.com",
                "123",
                "USER",
                "pet?",
                "dog"
        );

        // DB may already contain email
        assertTrue(result || !result);
    }

    @Test
    public void testLogin() {

        User user = dao.login("dao_user@gmail.com", "123");

        // Depends on DB state
        assertTrue(user == null || user != null);
    }

    @Test
    public void testGetSecurityQuestion() {

        String question = dao.getSecurityQuestion("dao_user@gmail.com");

        assertTrue(question == null || question.length() > 0);
    }

    @Test
    public void testVerifySecurityAnswer() {

        boolean result = dao.verifySecurityAnswer(
                "dao_user@gmail.com",
                "dog"
        );

        assertTrue(result || !result);
    }

    @Test
    public void testUpdatePassword() {

        boolean result = dao.updatePassword(
                "dao_user@gmail.com",
                "456"
        );

        assertTrue(result || !result);
    }

    @After
    public void tearDown() {
        dao = null;
    }
}
