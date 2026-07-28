package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.Reminder;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for the "reminders" table. */
public class ReminderDAO {

    public List<Reminder> findByUser(int userId) {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT r.*, m.medicine_name FROM reminders r " +
                     "JOIN medicines m ON r.medicine_id = m.medicine_id " +
                     "WHERE r.user_id=? ORDER BY r.remind_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("reminders findByUser failed", e);
        }
        return list;
    }

    public void add(Reminder r) {
        String sql = "INSERT INTO reminders(user_id, medicine_id, frequency, remind_time, detail) " +
                     "VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getMedicineId());
            ps.setString(3, r.getFrequency());
            ps.setString(4, r.getRemindTime());
            ps.setString(5, r.getDetail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("reminder add failed", e);
        }
    }

    /** Flip active on/off (scoped to the owning user). */
    public void toggle(int reminderId, int userId) {
        String sql = "UPDATE reminders SET active = NOT active WHERE reminder_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reminderId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("reminder toggle failed", e);
        }
    }

    public void delete(int reminderId, int userId) {
        String sql = "DELETE FROM reminders WHERE reminder_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reminderId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("reminder delete failed", e);
        }
    }

    private Reminder map(ResultSet rs) throws SQLException {
        Reminder r = new Reminder();
        r.setReminderId(rs.getInt("reminder_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setMedicineId(rs.getInt("medicine_id"));
        r.setMedicineName(rs.getString("medicine_name"));
        r.setFrequency(rs.getString("frequency"));
        Time t = rs.getTime("remind_time");
        r.setRemindTime(t == null ? "" : t.toString().substring(0, 5));  // HH:MM
        r.setDetail(rs.getString("detail"));
        r.setActive(rs.getBoolean("active"));
        return r;
    }
}
