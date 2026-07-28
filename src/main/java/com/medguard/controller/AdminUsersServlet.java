package com.medguard.controller;

import com.medguard.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: user management (search + pagination + delete). */
@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = req.getParameter("q") == null ? "" : req.getParameter("q").trim();
        int page;
        try { page = Math.max(1, Integer.parseInt(req.getParameter("page"))); }
        catch (Exception e) { page = 1; }

        int total = userDAO.count();
        req.setAttribute("q", q);
        req.setAttribute("page", page);
        req.setAttribute("pages", Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE)));
        req.setAttribute("users", userDAO.findPage(q, page, PAGE_SIZE));
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("delete".equals(req.getParameter("action"))) {
            userDAO.delete(Integer.parseInt(req.getParameter("id")));
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
