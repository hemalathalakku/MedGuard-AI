package com.medguard.controller;

import com.medguard.dao.MedicineDAO;
import com.medguard.validation.InputValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Returns medicine names matching ?term= as a small JSON array:
 * [{"id":1,"name":"Ibuprofen"}, ...]  - consumed by js/autocomplete.js.
 * JSON is built by hand (escaped) to keep the project dependency-free.
 */
@WebServlet("/user/autocomplete")
public class AutocompleteServlet extends HttpServlet {

    private final MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String term = req.getParameter("term");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        if (term == null || term.trim().length() < 2) {
            out.print("[]");
            return;
        }
        List<String[]> rows = medicineDAO.namesLike(term.trim(), 8);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) json.append(',');
            json.append("{\"id\":").append(rows.get(i)[0])
                .append(",\"name\":\"")
                .append(jsonEscape(rows.get(i)[1]))
                .append("\"}");
        }
        out.print(json.append(']').toString());
    }

    /** Escapes quotes/backslashes so names can't break the JSON (XSS-safe). */
    private String jsonEscape(String s) {
        return InputValidator.escapeHtml(s.replace("\\", "\\\\").replace("\"", "\\\""));
    }
}
