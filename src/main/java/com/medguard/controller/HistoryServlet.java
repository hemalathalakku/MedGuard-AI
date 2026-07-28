package com.medguard.controller;

import com.medguard.dao.HistoryDAO;
import com.medguard.model.HistoryEntry;
import com.medguard.util.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/** Module 12: searchable safety-check history with CSV export (?export=csv). */
@WebServlet("/user/history")
public class HistoryServlet extends HttpServlet {

    private final HistoryDAO historyDAO = new HistoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute(AppConstants.SESSION_USER_ID);
        String search = req.getParameter("q");
        List<HistoryEntry> list = historyDAO.findByUser(userId, search);

        if ("csv".equals(req.getParameter("export"))) {
            exportCsv(resp, list);
            return;
        }
        req.setAttribute("q", search == null ? "" : search);
        req.setAttribute("list", list);
        req.getRequestDispatcher("/WEB-INF/views/user/history.jsp").forward(req, resp);
    }

    /** Streams the history as a downloadable CSV file. */
    private void exportCsv(HttpServletResponse resp, List<HistoryEntry> list) throws IOException {
        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=medguard-history.csv");
        PrintWriter out = resp.getWriter();
        out.println("Medicine,Result,Warnings,CheckedAt");
        for (HistoryEntry h : list) {
            out.println(csv(h.getMedicineName()) + "," + csv(h.getResult()) + ","
                    + csv(h.getWarnings()) + "," + csv(h.getCheckedAt()));
        }
    }

    /** Quotes a CSV field and doubles inner quotes (RFC 4180). */
    private String csv(String s) {
        if (s == null) return "\"\"";
        return "\"" + s.replace("\"", "\"\"").replace("\n", " | ") + "\"";
    }
}
