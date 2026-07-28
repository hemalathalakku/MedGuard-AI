package com.medguard.controller;

import com.medguard.dao.CurrentMedicineDAO;
import com.medguard.dao.MedicineDAO;
import com.medguard.model.CurrentMedicine;
import com.medguard.util.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 7: CRUD for the medicines the user currently takes. */
@WebServlet("/user/current")
public class CurrentMedicineServlet extends HttpServlet {

    private final CurrentMedicineDAO currentDAO = new CurrentMedicineDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        req.setAttribute("list", currentDAO.findByUser(userId));
        req.setAttribute("allMeds", medicineDAO.search("", 1, 500));  // dropdown options
        req.getRequestDispatcher("/WEB-INF/views/user/currentMedicines.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            currentDAO.delete(Integer.parseInt(req.getParameter("id")), userId);
        } else {   // add
            CurrentMedicine c = new CurrentMedicine();
            c.setUserId(userId);
            c.setMedicineId(Integer.parseInt(req.getParameter("medicineId")));
            c.setDose(req.getParameter("dose"));
            c.setFrequency(req.getParameter("frequency"));
            String dur = req.getParameter("durationDays");
            c.setDurationDays(dur == null || dur.isBlank() ? null : Integer.valueOf(dur.trim()));
            c.setDoctorName(req.getParameter("doctorName"));
            c.setNotes(req.getParameter("notes"));
            c.setStartedOn(req.getParameter("startedOn"));
            currentDAO.add(c);
        }
        resp.sendRedirect(req.getContextPath() + "/user/current");
    }
}
