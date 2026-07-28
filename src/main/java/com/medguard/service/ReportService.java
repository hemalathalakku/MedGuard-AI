package com.medguard.service;

import com.medguard.dao.HistoryDAO;
import com.medguard.dao.MedicineDAO;
import com.medguard.dao.UserDAO;

import java.util.List;

/** Aggregated numbers for the admin dashboard and reports (Modules 13-14). */
public class ReportService {

    private final UserDAO userDAO = new UserDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();

    public int totalUsers()      { return userDAO.count(); }
    public int totalMedicines()  { return medicineDAO.count(); }
    public int totalChecks()     { return historyDAO.countAll(); }

    /** Rows of [SAFE|CAUTION|DANGEROUS, count] - feeds the doughnut chart. */
    public List<String[]> resultStats() { return historyDAO.resultStats(); }

    /** Rows of [medicine, count] - feeds the bar chart. */
    public List<String[]> topMedicines() { return historyDAO.topMedicines(5); }
}
