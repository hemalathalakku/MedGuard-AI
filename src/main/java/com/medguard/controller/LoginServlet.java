package com.medguard.controller;

import com.medguard.model.User;
import com.medguard.service.AuthService;
import com.medguard.util.AppConstants;
import com.medguard.util.CsrfUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** User login: stores userId + name in the session and issues a CSRF token. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = authService.login(req.getParameter("email"), req.getParameter("password"));
        if (u == null) {
            resp.sendRedirect("login.jsp?error=1");
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute(AppConstants.SESSION_USER_ID, u.getUserId());
        session.setAttribute(AppConstants.SESSION_USER_NAME, u.getFullName());
        session.setMaxInactiveInterval(30 * 60);   // 30-minute session timeout
        CsrfUtil.issueToken(session);
        resp.sendRedirect("user/dashboard");
    }
}
