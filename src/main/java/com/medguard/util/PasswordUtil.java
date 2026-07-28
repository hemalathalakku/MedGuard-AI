package com.medguard.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes passwords with SHA-256 so plain-text passwords are NEVER stored.
 * Used by the registration and login code in Module 2.
 */
public final class PasswordUtil {

    private PasswordUtil() { }

    /** @return 64-character lowercase hex SHA-256 hash of the input. */
    public static String hash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(64);
            for (byte b : digest) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed on every JVM, so this should never happen.
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /** Compares a plain password with a stored hash. */
    public static boolean matches(String plainText, String storedHash) {
        return hash(plainText).equals(storedHash);
    }
}
