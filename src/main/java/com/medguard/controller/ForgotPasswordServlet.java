package com.medguard.controller;

import com.medguard.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Forgot-password flow (Module 2).
 * POST action=request : creates a reset token for the email.
 *   EDUCATIONAL NOTE: with no mail server, the reset link is shown on screen.
 *   In production the link would be emailed instead - nothing else changes.
 * POST action=reset   : validates the token and saves the new password.
 */
@WebServlet("/forgot")
public class ForgotPasswordServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("reset".equals(action)) {
            boolean ok = authService.resetPassword(req.getParameter("token"),
                                                   req.getParameter("password"));
            resp.sendRedirect(ok ? "login.jsp?reset=1" : "forgot.jsp?error=badtoken");
            return;
        }

        // default action: request a token
        String token = authService.createResetToken(req.getParameter("email"));
        if (token == null) {
            resp.sendRedirect("forgot.jsp?error=noaccount");
        } else {
            // Show the token-entry form pre-filled (stands in for the email link).
            resp.sendRedirect("forgot.jsp?token=" + token);
        }
    }
}
