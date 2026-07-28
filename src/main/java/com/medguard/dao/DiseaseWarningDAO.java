package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for drug-disease warning rules. */
public class DiseaseWarningDAO {

    /** One rule row: severity + reason + display names. */
    public static class Rule {
        public int id; public String medicine; public String disease; public String severity; public String reason;
        public int getId(){return id;} public String getMedicine(){return medicine;}
        public String getDisease(){return disease;} public String getSeverity(){return severity;}
        public String getReason(){return reason;}
    }

    /** Engine query: all warnings for one medicine restricted to the user's diseases. */
    public List<Rule> findForMedicineAndUser(int medicineId, int userId) {
        List<Rule> list = new ArrayList<>();
        String sql = "SELECT w.warning_id, m.medicine_name, d.disease_name, w.severity, w.reason " +
                     "FROM medicine_disease_warnings w " +
                     "JOIN medicines m ON w.medicine_id = m.medicine_id " +
                     "JOIN diseases d ON w.disease_id = d.disease_id " +
                     "JOIN user_diseases ud ON ud.disease_id = w.disease_id " +
                     "WHERE w.medicine_id=? AND ud.user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("warnings findForMedicineAndUser failed", e);
        }
        return list;
    }

    public List<Rule> findAll() {
        List<Rule> list = new ArrayList<>();
        String sql = "SELECT w.warning_id, m.medicine_name, d.disease_name, w.severity, w.reason " +
                     "FROM medicine_disease_warnings w " +
                     "JOIN medicines m ON w.medicine_id = m.medicine_id " +
                     "JOIN diseases d ON w.disease_id = d.disease_id ORDER BY w.warning_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("warnings findAll failed", e);
        }
        return list;
    }

    public void insert(int medicineId, int diseaseId, String severity, String reason) {
        String sql = "INSERT INTO medicine_disease_warnings(medicine_id, disease_id, severity, reason) " +
                     "VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            ps.setInt(2, diseaseId);
            ps.setString(3, severity);
            ps.setString(4, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("warning insert failed", e);
        }
    }

    public void delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM medicine_disease_warnings WHERE warning_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("warning delete failed", e);
        }
    }

    private Rule map(ResultSet rs) throws SQLException {
        Rule r = new Rule();
        r.id = rs.getInt("warning_id");
        r.medicine = rs.getString("medicine_name");
        r.disease = rs.getString("disease_name");
        r.severity = rs.getString("severity");
        r.reason = rs.getString("reason");
        return r;
    }
}
