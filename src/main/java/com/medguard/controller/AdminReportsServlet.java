package com.medguard.controller;

import com.medguard.service.ReportService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 14: report data (feeds the Chart.js charts on the reports page). */
@WebServlet("/admin/reports")
public class AdminReportsServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("totalUsers", reportService.totalUsers());
        req.setAttribute("totalMedicines", reportService.totalMedicines());
        req.setAttribute("totalChecks", reportService.totalChecks());
        req.setAttribute("resultStats", reportService.resultStats());
        req.setAttribute("topMedicines", reportService.topMedicines());
        req.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(req, resp);
    }
}
