package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.util.DBConnection;
import com.medguard.util.PasswordUtil;

import java.sql.*;

/** JDBC access for the "admin" table. */
public class AdminDAO {

    /** @return the admin username if credentials are valid, otherwise null. */
    public String login(String username, String password) {
        String sql = "SELECT password FROM admin WHERE username=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.matches(password, rs.getString("password"))) {
                    return username;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("admin login failed", e);
        }
        return null;
    }
}
