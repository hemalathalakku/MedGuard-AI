package com.medguard.controller;

import com.medguard.dao.MedicineDAO;
import com.medguard.model.Medicine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 13: full medicine CRUD with search + pagination. */
@WebServlet("/admin/medicines")
public class AdminMedicinesServlet extends HttpServlet {

    private static final int PAGE_SIZE = 8;
    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Edit mode: load one medicine into the form
        String editId = req.getParameter("edit");
        if (editId != null) {
            req.setAttribute("edit", medicineDAO.findById(Integer.parseInt(editId)));
        }

        String q = req.getParameter("q") == null ? "" : req.getParameter("q").trim();
        int page;
        try { page = Math.max(1, Integer.parseInt(req.getParameter("page"))); }
        catch (Exception e) { page = 1; }

        int total = medicineDAO.countSearch(q);
        req.setAttribute("q", q);
        req.setAttribute("page", page);
        req.setAttribute("pages", Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE)));
        req.setAttribute("list", medicineDAO.search(q, page, PAGE_SIZE));
        req.getRequestDispatcher("/WEB-INF/views/admin/medicines.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            medicineDAO.delete(Integer.parseInt(req.getParameter("id")));
        } else {
            Medicine m = new Medicine();
            m.setMedicineName(req.getParameter("medicineName"));
            m.setGenericName(req.getParameter("genericName"));
            m.setBrand(req.getParameter("brand"));
            m.setManufacturer(req.getParameter("manufacturer"));
            m.setIngredients(req.getParameter("ingredients"));
            m.setDescription(req.getParameter("description"));
            m.setUses(req.getParameter("uses"));
            m.setDosage(req.getParameter("dosage"));
            m.setSideEffects(req.getParameter("sideEffects"));
            m.setContraindications(req.getParameter("contraindications"));
            m.setWarnings(req.getParameter("warnings"));
            m.setPregnancyCategory(req.getParameter("pregnancyCategory"));
            m.setStorageInstructions(req.getParameter("storageInstructions"));
            m.setAgeRestrictions(req.getParameter("ageRestrictions"));

            if ("update".equals(action)) {
                m.setMedicineId(Integer.parseInt(req.getParameter("id")));
                medicineDAO.update(m);
            } else {
                medicineDAO.insert(m);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/medicines");
    }
}
