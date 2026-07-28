package com.medguard.controller;

import com.medguard.dao.MedicineDAO;
import com.medguard.model.Medicine;
import com.medguard.model.SafetyResult;
import com.medguard.service.SafetyService;
import com.medguard.util.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Modules 8+9+10: runs the SafetyService against one medicine (?id=)
 * and forwards to the result page. The check is stored in history by the
 * service itself.
 */
@WebServlet("/user/safety")
public class SafetyCheckServlet extends HttpServlet {

    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final SafetyService safetyService = new SafetyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        Medicine m = medicineDAO.findById(Integer.parseInt(req.getParameter("id")));
        if (m == null) {
            resp.sendRedirect(req.getContextPath() + "/user/medicines");
            return;
        }
        SafetyResult result = safetyService.check(userId, m);
        req.setAttribute("m", m);
        req.setAttribute("result", result);
        req.getRequestDispatcher("/WEB-INF/views/user/safetyResult.jsp").forward(req, resp);
    }
}
