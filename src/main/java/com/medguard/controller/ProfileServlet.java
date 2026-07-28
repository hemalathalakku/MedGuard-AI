package com.medguard.controller;

import com.medguard.dao.UserDAO;
import com.medguard.model.User;
import com.medguard.util.AppConstants;
import com.medguard.validation.InputValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;

/** Module 4: view + edit profile, including an optional photo upload. */
@WebServlet("/user/profile")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)   // 2 MB photo limit
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        req.setAttribute("u", userDAO.findById(userId));
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        User u = userDAO.findById(userId);

        if (!InputValidator.notBlank(req.getParameter("fullName"))) {
            resp.sendRedirect(req.getContextPath() + "/user/profile?error=name");
            return;
        }
        u.setFullName(req.getParameter("fullName").trim());
        u.setAge(parseIntOrNull(req.getParameter("age")));
        u.setGender(req.getParameter("gender"));
        u.setHeightCm(parseDoubleOrNull(req.getParameter("heightCm")));
        u.setWeightKg(parseDoubleOrNull(req.getParameter("weightKg")));
        u.setBloodGroup(req.getParameter("bloodGroup"));
        u.setEmergencyContact(req.getParameter("emergencyContact"));
        u.setAddress(req.getParameter("address"));

        // Optional photo upload - saved under /uploads with a unique name.
        Part photo = req.getPart("photo");
        if (photo != null && photo.getSize() > 0) {
            String submitted = photo.getSubmittedFileName();
            String ext = (submitted != null && submitted.contains("."))
                    ? submitted.substring(submitted.lastIndexOf('.')) : ".jpg";
            String fileName = "u" + userId + "_" + System.currentTimeMillis() + ext;
            String dir = getServletContext().getRealPath("/uploads");
            new File(dir).mkdirs();
            photo.write(dir + File.separator + fileName);
            u.setPhoto(fileName);
        }

        userDAO.updateProfile(u);
        req.getSession().setAttribute(AppConstants.SESSION_USER_NAME, u.getFullName());
        resp.sendRedirect(req.getContextPath() + "/user/profile?saved=1");
    }

    private Integer parseIntOrNull(String s) {
        try { return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
    private Double parseDoubleOrNull(String s) {
        try { return (s == null || s.isBlank()) ? null : Double.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
