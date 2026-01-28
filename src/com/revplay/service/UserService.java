package com.revplay.service;

import com.revplay.dao.UserDAO;
import com.revplay.model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String name,
                            String email,
                            String password,
                            String role,
                            String securityQuestion,
                            String securityAnswer) {

        if (name == null || name.trim().isEmpty()) return false;
        if (!isValidEmail(email)) return false;
        if (!isValidPassword(password)) return false;
        if (!("USER".equals(role) || "ARTIST".equals(role))) return false;

        return userDAO.registerUser(
                name.trim(),
                email.trim().toLowerCase(),
                password,
                role,
                securityQuestion,
                securityAnswer
        );
    }

    public User login(String email, String password) {
        if (!isValidEmail(email)) return null;
        if (password == null || password.trim().isEmpty()) return null;

        return userDAO.login(
                email.trim().toLowerCase(),
                password
        );
    }

    public String getSecurityQuestion(String email) {
        if (!isValidEmail(email)) return null;

        return userDAO.getSecurityQuestion(
                email.trim().toLowerCase()
        );
    }

    public boolean verifySecurityAnswer(String email, String answer) {
        if (!isValidEmail(email)) return false;
        if (answer == null || answer.trim().isEmpty()) return false;

        return userDAO.verifySecurityAnswer(
                email.trim().toLowerCase(),
                answer.trim()
        );
    }

    public boolean updatePassword(String email, String newPassword) {
        if (!isValidEmail(email)) return false;
        if (!isValidPassword(newPassword)) return false;

        return userDAO.updatePassword(
                email.trim().toLowerCase(),
                newPassword
        );
    }


    private boolean isValidEmail(String email) {
        if (email == null) return false;

        String e = email.trim();
        return e.contains("@") && e.contains(".") && e.length() >= 6;
    }

    private boolean isValidPassword(String pass) {
        if (pass == null) return false;

        return pass.trim().length() >= 4;
    }
}
