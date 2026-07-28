package com.medguard.validation;

import java.util.regex.Pattern;

/**
 * Server-side validation helpers. Module 2 (authentication) uses these;
 * client-side JS mirrors the same rules for instant feedback.
 */
public final class InputValidator {

    private InputValidator() { }

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /** Password rule: at least 8 chars, one letter and one digit. */
    private static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD.matcher(password).matches();
    }

    public static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /** Very small XSS guard: escape HTML special characters before display. */
    public static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
