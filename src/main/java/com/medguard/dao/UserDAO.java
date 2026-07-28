package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.User;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** All JDBC access for the "users" table. Uses PreparedStatement everywhere. */
public class UserDAO {

    /** @return generated user_id, or -1 if the email is already registered. */
    public int register(User u, String passwordHash) {
        if (findByEmail(u.getEmail()) != null) return -1;
        String sql = "INSERT INTO users(full_name,email,password) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, passwordHash);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new DataAccessException("register user failed", e);
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByEmail failed", e);
        }
    }

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById failed", e);
        }
    }

    public void updateProfile(User u) {
        String sql = "UPDATE users SET full_name=?, age=?, gender=?, height_cm=?, weight_kg=?, " +
                     "blood_group=?, emergency_contact=?, address=?, photo=? WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            if (u.getAge() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, u.getAge());
            ps.setString(3, u.getGender());
            if (u.getHeightCm() == null) ps.setNull(4, Types.DECIMAL); else ps.setDouble(4, u.getHeightCm());
            if (u.getWeightKg() == null) ps.setNull(5, Types.DECIMAL); else ps.setDouble(5, u.getWeightKg());
            ps.setString(6, u.getBloodGroup());
            ps.setString(7, u.getEmergencyContact());
            ps.setString(8, u.getAddress());
            ps.setString(9, u.getPhoto());
            ps.setInt(10, u.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("updateProfile failed", e);
        }
    }

    public void setResetToken(int userId, String token) {
        String sql = "UPDATE users SET reset_token=? WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("setResetToken failed", e);
        }
    }

    /** Finds the user holding this reset token (null if the token is invalid). */
    public User findByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByResetToken failed", e);
        }
    }

    /** Saves a new password hash and clears the reset token in one statement. */
    public void updatePassword(int userId, String newHash) {
        String sql = "UPDATE users SET password=?, reset_token=NULL WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("updatePassword failed", e);
        }
    }

    /** Paged list for the admin panel. page starts at 1. */
    public List<User> findPage(String search, int page, int pageSize) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE full_name LIKE ? OR email LIKE ? " +
                     "ORDER BY user_id DESC LIMIT ? OFFSET ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + (search == null ? "" : search) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setInt(3, pageSize);
            ps.setInt(4, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("findPage failed", e);
        }
        return list;
    }

    public int count() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("count users failed", e);
        }
    }

    public void delete(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("delete user failed", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        int age = rs.getInt("age");             u.setAge(rs.wasNull() ? null : age);
        u.setGender(rs.getString("gender"));
        double h = rs.getDouble("height_cm");   u.setHeightCm(rs.wasNull() ? null : h);
        double w = rs.getDouble("weight_kg");   u.setWeightKg(rs.wasNull() ? null : w);
        u.setBloodGroup(rs.getString("blood_group"));
        u.setEmergencyContact(rs.getString("emergency_contact"));
        u.setAddress(rs.getString("address"));
        u.setPhoto(rs.getString("photo"));
        return u;
    }
}
