package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** JDBC access for the "allergies" table (one row per allergy per user). */
public class AllergyDAO {

    /** @return map of allergy_id -> allergy_name for one user (insertion-ordered). */
    public Map<Integer, String> findByUser(int userId) {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT allergy_id, allergy_name FROM allergies WHERE user_id=? ORDER BY allergy_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) map.put(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new DataAccessException("allergies findByUser failed", e);
        }
        return map;
    }

    /** Just the names - what the allergy checker needs. */
    public List<String> namesByUser(int userId) {
        return new ArrayList<>(findByUser(userId).values());
    }

    public void add(int userId, String name) {
        String sql = "INSERT INTO allergies(user_id, allergy_name) VALUES(?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("allergy add failed", e);
        }
    }

    /** Delete by id AND user id so one user can never delete another user's row. */
    public void delete(int allergyId, int userId) {
        String sql = "DELETE FROM allergies WHERE allergy_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, allergyId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("allergy delete failed", e);
        }
    }
}
