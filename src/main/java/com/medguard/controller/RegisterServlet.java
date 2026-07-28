package com.medguard.controller;

import com.medguard.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Handles user sign-up. The form lives at /register.jsp. */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = authService.register(req.getParameter("name"),
                                      req.getParameter("email"),
                                      req.getParameter("password"));
        if (id == -2)      resp.sendRedirect("register.jsp?error=invalid");
        else if (id == -1) resp.sendRedirect("register.jsp?error=exists");
        else               resp.sendRedirect("login.jsp?registered=1");
    }
}
