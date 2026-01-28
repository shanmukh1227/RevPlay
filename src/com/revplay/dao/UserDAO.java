package com.revplay.dao;

import com.revplay.db.DBConnection;
import com.revplay.model.User;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDAO {

    private static final Logger log =
            Logger.getLogger(UserDAO.class);

    public boolean registerUser(String name,
                                String email,
                                String password,
                                String role,
                                String securityQuestion,
                                String securityAnswer) {

        String sql ="INSERT INTO users(name,email,password,role,security_question,security_answer) " +"VALUES(?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setString(5, securityQuestion);
            ps.setString(6, securityAnswer);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("User registered: " + email + " (role=" + role + ")");
                return true;
            }

            log.warn("Registration failed for email: " + email);
            return false;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Email already exists!");
            log.warn("Registration failed - email already exists: " + email);
            return false;

        } catch (SQLException e) {
            log.error("Registration error for email: " + email, e);
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public User login(String email, String password) {

        String sql ="SELECT user_id, name, email, role " +"FROM users WHERE email=? AND password=?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User(rs.getInt("user_id"),rs.getString("name"),rs.getString("email"),rs.getString("role"));
                log.info("Login success: " + email +" (userId=" + u.getUserId() + ")");
                return u;
            }

            log.warn("Login failed: " + email);
            return null;

        } catch (SQLException e) {
            log.error("Login error for email: " + email, e);
            e.printStackTrace();
            return null;

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSecurityQuestion(String email) {

        String sql ="SELECT security_question FROM users WHERE email=?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);

            rs = ps.executeQuery();
            if (rs.next()) {
                log.info("Security question fetched for " + email);
                return rs.getString("security_question");
            }

            log.warn("Security question not found for email: " + email);
            return null;

        } catch (SQLException e) {
            log.error("Error fetching security question for " + email, e);
            e.printStackTrace();
            return null;

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verifySecurityAnswer(String email, String answer) {

        String sql =
            "SELECT 1 FROM users WHERE email=? AND security_answer=?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, answer);

            rs = ps.executeQuery();
            boolean ok = rs.next();

            if (ok) {
                log.info("Security answer verified for " + email);
            } else {
                log.warn("Security answer incorrect for " + email);
            }

            return ok;

        } catch (SQLException e) {
            log.error("Error verifying security answer for " + email, e);
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePassword(String email, String newPassword) {

        String sql =
            "UPDATE users SET password=? WHERE email=?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setString(2, email);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Password updated for " + email);
                return true;
            }

            log.warn("Password update failed for email: " + email);
            return false;

        } catch (SQLException e) {
            log.error("Password update error for " + email, e);
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
