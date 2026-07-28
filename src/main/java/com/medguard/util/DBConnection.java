package com.medguard.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single place that opens JDBC connections to MySQL.
 * Every DAO in later modules calls DBConnection.getConnection().
 *
 * HOW TO CONFIGURE:
 * change USER / PASS below to match your local MySQL installation.
 */
public final class DBConnection {

    // ====== EDIT THESE to match your MySQL setup ======
    private static final String URL =
            "jdbc:mysql://localhost:3306/medguard?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Hema@sql07";
    // ==================================================

    private DBConnection() { }   // utility class - never instantiated

    // Load the MySQL driver once, the first time this class is used.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC driver not found on the classpath: " + e.getMessage());
        }
    }

    /** @return a NEW open connection. Caller must close it (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /** Quick standalone test:  java ... com.medguard.util.DBConnection */
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("SUCCESS: connected to " + con.getMetaData().getURL());
        } catch (SQLException e) {
            System.out.println("FAILED: " + e.getMessage());
        }
    }
}
