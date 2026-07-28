package com.medguard.controller;

import com.medguard.dao.*;
import com.medguard.util.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 3: gathers everything the user dashboard displays, then forwards. */
@WebServlet("/user/dashboard")
public class DashboardServlet extends HttpServlet {

    private final CurrentMedicineDAO currentDAO = new CurrentMedicineDAO();
    private final ReminderDAO reminderDAO = new ReminderDAO();
    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();
    private final AllergyDAO allergyDAO = new AllergyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);

        req.setAttribute("currentMeds", currentDAO.findByUser(userId));
        req.setAttribute("reminders", reminderDAO.findByUser(userId));
        req.setAttribute("diseases", diseaseDAO.findByUser(userId));
        req.setAttribute("allergies", allergyDAO.findByUser(userId));
        req.setAttribute("recent", historyDAO.recent(userId, 5));

        req.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(req, resp);
    }
}
