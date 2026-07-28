package com.medguard.controller;

import com.medguard.dao.DiseaseDAO;
import com.medguard.dao.DiseaseWarningDAO;
import com.medguard.dao.MedicineDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: manage drug-disease warning rules. */
@WebServlet("/admin/warnings")
public class AdminWarningsServlet extends HttpServlet {

    private final DiseaseWarningDAO warningDAO = new DiseaseWarningDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final DiseaseDAO diseaseDAO = new DiseaseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("rules", warningDAO.findAll());
        req.setAttribute("allMeds", medicineDAO.search("", 1, 500));
        req.setAttribute("allDiseases", diseaseDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/admin/warnings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("delete".equals(req.getParameter("action"))) {
            warningDAO.delete(Integer.parseInt(req.getParameter("id")));
        } else {
            warningDAO.insert(Integer.parseInt(req.getParameter("medicineId")),
                              Integer.parseInt(req.getParameter("diseaseId")),
                              req.getParameter("severity"),
                              req.getParameter("reason"));
        }
        resp.sendRedirect(req.getContextPath() + "/admin/warnings");
    }
}
