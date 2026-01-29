package com.revplay.service;

import com.revplay.dao.UserDAO;
import com.revplay.model.User;
import com.revplay.exception.UserServiceException;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // for unit testing (Mockito)
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(String name, String email, String password,
                            String role, String securityQuestion,
                            String securityAnswer) {

        if (name == null || name.trim().isEmpty()) {
            throw new UserServiceException("Name cannot be empty");
        }

        if (!isValidEmail(email)) {
            throw new UserServiceException("Invalid email format");
        }

        if (!isValidPassword(password)) {
            throw new UserServiceException("Password must be at least 3 characters");
        }

        if (!("USER".equals(role) || "ARTIST".equals(role))) {
            throw new UserServiceException("Invalid role");
        }

        boolean success = userDAO.registerUser(
                name.trim(),
                email.trim().toLowerCase(),
                password,
                role,
                securityQuestion,
                securityAnswer
        );

        if (!success) {
            throw new UserServiceException("User already exists");
        }

        return true;
    }

    public User login(String email, String password) {

        if (!isValidEmail(email)) {
            throw new UserServiceException("Invalid email");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new UserServiceException("Password cannot be empty");
        }

        User user = userDAO.login(email.trim().toLowerCase(), password);

        if (user == null) {
            throw new UserServiceException("Invalid credentials");
        }

        return user;
    }
    

    public String getSecurityQuestion(String email) {

        if (!isValidEmail(email)) {
            throw new UserServiceException("Invalid email");
        }

        String question = userDAO.getSecurityQuestion(email.trim().toLowerCase());

        if (question == null) {
            throw new UserServiceException("Email not found");
        }

        return question;
    }

    public boolean verifySecurityAnswer(String email, String answer) {

        if (!isValidEmail(email)) {
            throw new UserServiceException("Invalid email");
        }

        if (answer == null || answer.trim().isEmpty()) {
            throw new UserServiceException("Answer cannot be empty");
        }

        boolean ok = userDAO.verifySecurityAnswer(
                email.trim().toLowerCase(),
                answer.trim()
        );

        if (!ok) {
            throw new UserServiceException("Security answer incorrect");
        }

        return true;
    }

    public boolean updatePassword(String email, String newPassword) {

        if (!isValidEmail(email)) {
            throw new UserServiceException("Invalid email");
        }

        if (!isValidPassword(newPassword)) {
            throw new UserServiceException("Invalid password");
        }

        boolean ok = userDAO.updatePassword(
                email.trim().toLowerCase(),
                newPassword
        );

        if (!ok) {
            throw new UserServiceException("Password update failed");
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        if (email == null)
            return false;

        String e = email.trim();
        return e.contains("@") && e.contains(".") && e.length() >= 6;
    }

    private boolean isValidPassword(String pass) {
        if (pass == null)
            return false;

        return pass.trim().length() >= 3;
    }
}
