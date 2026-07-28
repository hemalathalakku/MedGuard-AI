package com.medguard.controller;

import com.medguard.dao.InteractionDAO;
import com.medguard.dao.MedicineDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: manage drug-drug interaction rules. */
@WebServlet("/admin/interactions")
public class AdminInteractionsServlet extends HttpServlet {

    private final InteractionDAO interactionDAO = new InteractionDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("rules", interactionDAO.findAll());
        req.setAttribute("allMeds", medicineDAO.search("", 1, 500));
        req.getRequestDispatcher("/WEB-INF/views/admin/interactions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("delete".equals(req.getParameter("action"))) {
            interactionDAO.delete(Integer.parseInt(req.getParameter("id")));
        } else {
            int a = Integer.parseInt(req.getParameter("medicineA"));
            int b = Integer.parseInt(req.getParameter("medicineB"));
            if (a != b) {   // a medicine can't interact with itself
                interactionDAO.insert(a, b, req.getParameter("risk"), req.getParameter("reason"));
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/interactions");
    }
}
