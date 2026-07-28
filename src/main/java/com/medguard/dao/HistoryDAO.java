package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.HistoryEntry;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for the "medicine_history" table. */
public class HistoryDAO {

    public void save(int userId, int medicineId, String result, String warnings) {
        String sql = "INSERT INTO medicine_history(user_id, medicine_id, result, warnings) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, medicineId);
            ps.setString(3, result);
            ps.setString(4, warnings);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("history save failed", e);
        }
    }

    /** User's history, optionally filtered by medicine-name search. */
    public List<HistoryEntry> findByUser(int userId, String search) {
        List<HistoryEntry> list = new ArrayList<>();
        String sql = "SELECT h.*, m.medicine_name FROM medicine_history h " +
                     "JOIN medicines m ON h.medicine_id = m.medicine_id " +
                     "WHERE h.user_id=? AND m.medicine_name LIKE ? ORDER BY h.checked_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, "%" + (search == null ? "" : search) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("history findByUser failed", e);
        }
        return list;
    }

    /** Last N checks - shown on the dashboard as "recent activity". */
    public List<HistoryEntry> recent(int userId, int limit) {
        List<HistoryEntry> list = new ArrayList<>();
        String sql = "SELECT h.*, m.medicine_name FROM medicine_history h " +
                     "JOIN medicines m ON h.medicine_id = m.medicine_id " +
                     "WHERE h.user_id=? ORDER BY h.checked_at DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("history recent failed", e);
        }
        return list;
    }

    public int countAll() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM medicine_history")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("history countAll failed", e);
        }
    }

    /** Verdict distribution for admin charts: rows of [result, count]. */
    public List<String[]> resultStats() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT result, COUNT(*) c FROM medicine_history GROUP BY result";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
        } catch (SQLException e) {
            throw new DataAccessException("history resultStats failed", e);
        }
        return list;
    }

    /** Most-checked medicines for admin reports: rows of [medicine, count]. */
    public List<String[]> topMedicines(int limit) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT m.medicine_name, COUNT(*) c FROM medicine_history h " +
                     "JOIN medicines m ON h.medicine_id = m.medicine_id " +
                     "GROUP BY m.medicine_name ORDER BY c DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (SQLException e) {
            throw new DataAccessException("history topMedicines failed", e);
        }
        return list;
    }

    private HistoryEntry map(ResultSet rs) throws SQLException {
        HistoryEntry h = new HistoryEntry();
        h.setHistoryId(rs.getInt("history_id"));
        h.setUserId(rs.getInt("user_id"));
        h.setMedicineId(rs.getInt("medicine_id"));
        h.setMedicineName(rs.getString("medicine_name"));
        h.setResult(rs.getString("result"));
        h.setWarnings(rs.getString("warnings"));
        h.setCheckedAt(String.valueOf(rs.getTimestamp("checked_at")));
        return h;
    }
}
