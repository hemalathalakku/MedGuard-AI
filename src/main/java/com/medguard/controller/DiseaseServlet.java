package com.medguard.controller;

import com.medguard.dao.AllergyDAO;
import com.medguard.dao.DiseaseDAO;
import com.medguard.util.AppConstants;
import com.medguard.validation.InputValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 5 + 10 input side: manage the user's diseases and allergy list. */
@WebServlet("/user/diseases")
public class DiseaseServlet extends HttpServlet {

    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private final AllergyDAO allergyDAO = new AllergyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        req.setAttribute("allDiseases", diseaseDAO.findAll());
        req.setAttribute("myDiseases", diseaseDAO.findByUser(userId));
        req.setAttribute("myAllergies", allergyDAO.findByUser(userId));
        req.getRequestDispatcher("/WEB-INF/views/user/diseases.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        String action = req.getParameter("action");

        switch (action == null ? "" : action) {
            case "addDisease" ->
                diseaseDAO.addToUser(userId,
                        Integer.parseInt(req.getParameter("diseaseId")),
                        req.getParameter("diagnosedOn"));
            case "removeDisease" ->
                diseaseDAO.removeFromUser(userId, Integer.parseInt(req.getParameter("diseaseId")));
            case "addAllergy" -> {
                String name = req.getParameter("allergyName");
                if (InputValidator.notBlank(name)) allergyDAO.add(userId, name.trim());
            }
            case "removeAllergy" ->
                allergyDAO.delete(Integer.parseInt(req.getParameter("allergyId")), userId);
            default -> { /* unknown action: fall through to redirect */ }
        }
        resp.sendRedirect(req.getContextPath() + "/user/diseases");
    }
}
