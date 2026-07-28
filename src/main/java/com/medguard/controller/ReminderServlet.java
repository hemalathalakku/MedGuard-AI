package com.medguard.controller;

import com.medguard.dao.MedicineDAO;
import com.medguard.dao.ReminderDAO;
import com.medguard.model.Reminder;
import com.medguard.util.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 11: CRUD for daily / weekly / monthly medication reminders. */
@WebServlet("/user/reminders")
public class ReminderServlet extends HttpServlet {

    private final ReminderDAO reminderDAO = new ReminderDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        req.setAttribute("list", reminderDAO.findByUser(userId));
        req.setAttribute("allMeds", medicineDAO.search("", 1, 500));
        req.getRequestDispatcher("/WEB-INF/views/user/reminders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        String action = req.getParameter("action");

        switch (action == null ? "" : action) {
            case "toggle" -> reminderDAO.toggle(Integer.parseInt(req.getParameter("id")), userId);
            case "delete" -> reminderDAO.delete(Integer.parseInt(req.getParameter("id")), userId);
            default -> {   // add
                Reminder r = new Reminder();
                r.setUserId(userId);
                r.setMedicineId(Integer.parseInt(req.getParameter("medicineId")));
                r.setFrequency(req.getParameter("frequency"));
                r.setRemindTime(req.getParameter("remindTime"));
                r.setDetail(req.getParameter("detail"));
                reminderDAO.add(r);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/user/reminders");
    }
}
