package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.CurrentMedicine;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for the "current_medicines" table. */
public class CurrentMedicineDAO {

    public List<CurrentMedicine> findByUser(int userId) {
        List<CurrentMedicine> list = new ArrayList<>();
        String sql = "SELECT cm.*, m.medicine_name FROM current_medicines cm " +
                     "JOIN medicines m ON cm.medicine_id = m.medicine_id " +
                     "WHERE cm.user_id=? ORDER BY m.medicine_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("current meds findByUser failed", e);
        }
        return list;
    }

    public void add(CurrentMedicine c) {
        String sql = "INSERT IGNORE INTO current_medicines(user_id, medicine_id, dose, frequency, " +
                     "duration_days, doctor_name, notes, started_on) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getUserId());
            ps.setInt(2, c.getMedicineId());
            ps.setString(3, c.getDose());
            ps.setString(4, c.getFrequency());
            if (c.getDurationDays() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, c.getDurationDays());
            ps.setString(6, c.getDoctorName());
            ps.setString(7, c.getNotes());
            if (c.getStartedOn() == null || c.getStartedOn().isEmpty()) ps.setNull(8, Types.DATE);
            else ps.setDate(8, Date.valueOf(c.getStartedOn()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("current med add failed", e);
        }
    }

    /** id AND user_id in the WHERE clause = a user can only delete their own rows. */
    public void delete(int id, int userId) {
        String sql = "DELETE FROM current_medicines WHERE id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("current med delete failed", e);
        }
    }

    private CurrentMedicine map(ResultSet rs) throws SQLException {
        CurrentMedicine c = new CurrentMedicine();
        c.setId(rs.getInt("id"));
        c.setUserId(rs.getInt("user_id"));
        c.setMedicineId(rs.getInt("medicine_id"));
        c.setMedicineName(rs.getString("medicine_name"));
        c.setDose(rs.getString("dose"));
        c.setFrequency(rs.getString("frequency"));
        int d = rs.getInt("duration_days"); c.setDurationDays(rs.wasNull() ? null : d);
        c.setDoctorName(rs.getString("doctor_name"));
        c.setNotes(rs.getString("notes"));
        Date sd = rs.getDate("started_on");
        c.setStartedOn(sd == null ? "" : sd.toString());
        return c;
    }
}
