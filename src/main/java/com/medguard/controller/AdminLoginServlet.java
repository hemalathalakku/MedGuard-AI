package com.medguard.controller;

import com.medguard.service.AuthService;
import com.medguard.util.AppConstants;
import com.medguard.util.CsrfUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Admin login (role-based access: admin session key is separate from user's). */
@WebServlet("/adminlogin")
public class AdminLoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String admin = authService.adminLogin(req.getParameter("username"),
                                              req.getParameter("password"));
        if (admin == null) {
            resp.sendRedirect("admin/login.jsp?error=1");
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute(AppConstants.SESSION_ADMIN, admin);
        session.setMaxInactiveInterval(30 * 60);
        CsrfUtil.issueToken(session);
        resp.sendRedirect("admin/dashboard");
    }
}
