package com.medguard.controller;

import com.medguard.dao.MedicineDAO;
import com.medguard.model.Medicine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Module 6: search list (?q=&page=) and details view (?id=). */
@WebServlet("/user/medicines")
public class MedicineSearchServlet extends HttpServlet {

    private static final int PAGE_SIZE = 8;
    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Details mode when an id is supplied
        String id = req.getParameter("id");
        if (id != null) {
            Medicine m = medicineDAO.findById(Integer.parseInt(id));
            req.setAttribute("m", m);
            req.getRequestDispatcher("/WEB-INF/views/user/medicineDetails.jsp").forward(req, resp);
            return;
        }

        // Search-list mode
        String q = req.getParameter("q") == null ? "" : req.getParameter("q").trim();
        int page = parsePage(req.getParameter("page"));
        int total = medicineDAO.countSearch(q);
        int pages = Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE));

        req.setAttribute("q", q);
        req.setAttribute("page", page);
        req.setAttribute("pages", pages);
        req.setAttribute("list", medicineDAO.search(q, page, PAGE_SIZE));
        req.getRequestDispatcher("/WEB-INF/views/user/medicineSearch.jsp").forward(req, resp);
    }

    private int parsePage(String s) {
        try { return Math.max(1, Integer.parseInt(s)); }
        catch (Exception e) { return 1; }
    }
}
