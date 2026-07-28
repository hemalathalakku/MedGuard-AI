package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.Disease;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for "diseases" and the user_diseases link table. */
public class DiseaseDAO {

    public List<Disease> findAll() {
        List<Disease> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM diseases ORDER BY disease_name")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("diseases findAll failed", e);
        }
        return list;
    }

    /** Diseases attached to one user, with diagnosis date. */
    public List<Disease> findByUser(int userId) {
        List<Disease> list = new ArrayList<>();
        String sql = "SELECT d.*, ud.diagnosed_on FROM diseases d " +
                     "JOIN user_diseases ud ON d.disease_id = ud.disease_id " +
                     "WHERE ud.user_id=? ORDER BY d.disease_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Disease d = map(rs);
                    Date dt = rs.getDate("diagnosed_on");
                    d.setDiagnosedOn(dt == null ? "" : dt.toString());
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("diseases findByUser failed", e);
        }
        return list;
    }

    /** Attach a disease to a user; INSERT IGNORE avoids duplicate errors. */
    public void addToUser(int userId, int diseaseId, String diagnosedOn) {
        String sql = "INSERT IGNORE INTO user_diseases(user_id, disease_id, diagnosed_on) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, diseaseId);
            if (diagnosedOn == null || diagnosedOn.isEmpty()) ps.setNull(3, Types.DATE);
            else ps.setDate(3, Date.valueOf(diagnosedOn));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("addToUser failed", e);
        }
    }

    public void removeFromUser(int userId, int diseaseId) {
        String sql = "DELETE FROM user_diseases WHERE user_id=? AND disease_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, diseaseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("removeFromUser failed", e);
        }
    }

    // ---- admin CRUD on the master list ----
    public void insert(String name, String description) {
        String sql = "INSERT INTO diseases(disease_name, description) VALUES(?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("disease insert failed", e);
        }
    }

    public void delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM diseases WHERE disease_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("disease delete failed", e);
        }
    }

    private Disease map(ResultSet rs) throws SQLException {
        Disease d = new Disease();
        d.setDiseaseId(rs.getInt("disease_id"));
        d.setDiseaseName(rs.getString("disease_name"));
        d.setDescription(rs.getString("description"));
        return d;
    }
}
