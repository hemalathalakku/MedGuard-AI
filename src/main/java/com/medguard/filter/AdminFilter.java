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
 * Authorization filter for the ADMIN area (/admin/*).
 * The admin login page itself is excluded (otherwise nobody could ever log in).
 * Validates the CSRF token on every POST, same as AuthFilter.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();

        // The admin login page is public.
        if ("/admin/login.jsp".equals(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean adminLoggedIn = session != null
                && session.getAttribute(AppConstants.SESSION_ADMIN) != null;
        if (!adminLoggedIn) {
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
            return;
        }
        if ("POST".equalsIgnoreCase(request.getMethod()) && !CsrfUtil.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        chain.doFilter(req, res);
    }
}
