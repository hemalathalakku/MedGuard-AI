package com.medguard.controller;

import com.medguard.service.ReportService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: admin dashboard statistics. */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("totalUsers", reportService.totalUsers());
        req.setAttribute("totalMedicines", reportService.totalMedicines());
        req.setAttribute("totalChecks", reportService.totalChecks());
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}
