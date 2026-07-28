package com.medguard.filter;

import com.medguard.util.AppConstants;
import com.medguard.util.CsrfUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication filter for the USER area (/user/*).
 * 1. Redirects to login if there is no logged-in user in the session.
 * 2. Validates the CSRF token on every POST.
 */
@WebFilter("/user/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        boolean loggedIn = session != null
                && session.getAttribute(AppConstants.SESSION_USER_ID) != null;
        if (!loggedIn) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if ("POST".equalsIgnoreCase(request.getMethod()) && !CsrfUtil.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        chain.doFilter(req, res);
    }
}
