package com.medguard.controller;

import com.medguard.dao.DiseaseDAO;
import com.medguard.validation.InputValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: master disease list CRUD. */
@WebServlet("/admin/diseases")
public class AdminDiseasesServlet extends HttpServlet {

    private final DiseaseDAO diseaseDAO = new DiseaseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("diseases", diseaseDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/admin/diseases.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            diseaseDAO.delete(Integer.parseInt(req.getParameter("id")));
        } else if (InputValidator.notBlank(req.getParameter("name"))) {
            diseaseDAO.insert(req.getParameter("name").trim(), req.getParameter("description"));
        }
        resp.sendRedirect(req.getContextPath() + "/admin/diseases");
    }
}
