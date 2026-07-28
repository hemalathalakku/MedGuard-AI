package com.medguard.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Simple synchronizer-token CSRF protection.
 * A random token is created at login and stored in the session; every JSP form
 * includes it as a hidden field named "csrf"; the filters reject any POST under
 * /user/* or /admin/* whose token doesn't match.
 */
public final class CsrfUtil {

    public static final String SESSION_KEY = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() { }

    /** Create a token and store it in the session (called once at login). */
    public static void issueToken(HttpSession session) {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        session.setAttribute(SESSION_KEY, HexFormat.of().formatHex(bytes));
    }

    /** @return true when the request's "csrf" parameter matches the session token. */
    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Object expected = session.getAttribute(SESSION_KEY);
        String actual = request.getParameter("csrf");
        return expected != null && expected.equals(actual);
    }
}
