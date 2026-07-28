package com.medguard.util;

/**
 * Central place for constants used across modules
 * (session keys, the legal disclaimer, etc.).
 */
public final class AppConstants {

    private AppConstants() { }

    /** Session attribute holding the logged-in user's id. */
    public static final String SESSION_USER_ID = "userId";
    /** Session attribute holding the logged-in user's name. */
    public static final String SESSION_USER_NAME = "userName";
    /** Session attribute holding the logged-in admin's username. */
    public static final String SESSION_ADMIN = "adminUser";

    /** Shown on every medicine safety result page (required). */
    public static final String DISCLAIMER =
            "This application provides educational information only and is not a substitute " +
            "for professional medical advice, diagnosis, or treatment. Always consult a " +
            "qualified healthcare professional before starting, stopping, or changing any medication.";
}
