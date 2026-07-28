package com.medguard.service;

import com.medguard.dao.AdminDAO;
import com.medguard.dao.UserDAO;
import com.medguard.model.User;
import com.medguard.util.PasswordUtil;
import com.medguard.validation.InputValidator;

import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Business logic for authentication (Module 2).
 * Controllers never touch hashing or DAOs directly for auth flows.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private static final SecureRandom RANDOM = new SecureRandom();

    /** @return new user id, -1 = email taken, -2 = validation failed. */
    public int register(String name, String email, String password) {
        if (!InputValidator.notBlank(name)
                || !InputValidator.isValidEmail(email)
                || !InputValidator.isValidPassword(password)) return -2;
        User u = new User();
        u.setFullName(name.trim());
        u.setEmail(email.trim().toLowerCase());
        return userDAO.register(u, PasswordUtil.hash(password));
    }

    /** @return the User when credentials match, otherwise null. */
    public User login(String email, String password) {
        if (email == null || password == null) return null;
        User u = userDAO.findByEmail(email.trim().toLowerCase());
        if (u != null && PasswordUtil.matches(password, u.getPassword())) return u;
        return null;
    }

    /** @return admin username on success, otherwise null. */
    public String adminLogin(String username, String password) {
        if (username == null || password == null) return null;
        return adminDAO.login(username.trim(), password);
    }

    /**
     * Forgot-password step 1: create and store a random token for the account.
     * EDUCATIONAL NOTE: a production system would EMAIL this token as a link.
     * This project has no mail server, so the controller displays the link
     * directly - the flow and security model are otherwise identical.
     *
     * @return the token, or null if the email is not registered.
     */
    public String createResetToken(String email) {
        User u = userDAO.findByEmail(email == null ? "" : email.trim().toLowerCase());
        if (u == null) return null;
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        String token = HexFormat.of().formatHex(bytes);
        userDAO.setResetToken(u.getUserId(), token);
        return token;
    }

    /** Forgot-password step 2: set the new password if the token is valid. */
    public boolean resetPassword(String token, String newPassword) {
        if (!InputValidator.isValidPassword(newPassword)) return false;
        User u = userDAO.findByResetToken(token);
        if (u == null) return false;
        userDAO.updatePassword(u.getUserId(), PasswordUtil.hash(newPassword));
        return true;
    }
}
