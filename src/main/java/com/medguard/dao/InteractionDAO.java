package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for drug-drug interaction rules. */
public class InteractionDAO {

    /** One rule row for admin listing / the engine: [id, medA, medB, risk, reason]. */
    public static class Rule {
        public int id; public String medA; public String medB; public String risk; public String reason;
        public int getId(){return id;} public String getMedA(){return medA;}
        public String getMedB(){return medB;} public String getRisk(){return risk;}
        public String getReason(){return reason;}
    }

    /**
     * Engine query: find a rule between two medicines, checking BOTH directions
     * (a,b) and (b,a) so rule authors don't have to enter each pair twice.
     */
    public Rule findBetween(int medIdA, int medIdB) {
        String sql = "SELECT mi.*, ma.medicine_name an, mb.medicine_name bn FROM medicine_interactions mi " +
                     "JOIN medicines ma ON mi.medicine_a_id = ma.medicine_id " +
                     "JOIN medicines mb ON mi.medicine_b_id = mb.medicine_id " +
                     "WHERE (mi.medicine_a_id=? AND mi.medicine_b_id=?) " +
                     "   OR (mi.medicine_a_id=? AND mi.medicine_b_id=?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medIdA); ps.setInt(2, medIdB);
            ps.setInt(3, medIdB); ps.setInt(4, medIdA);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("interaction findBetween failed", e);
        }
    }

    public List<Rule> findAll() {
        List<Rule> list = new ArrayList<>();
        String sql = "SELECT mi.*, ma.medicine_name an, mb.medicine_name bn FROM medicine_interactions mi " +
                     "JOIN medicines ma ON mi.medicine_a_id = ma.medicine_id " +
                     "JOIN medicines mb ON mi.medicine_b_id = mb.medicine_id ORDER BY mi.interaction_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("interaction findAll failed", e);
        }
        return list;
    }

    public void insert(int medA, int medB, String risk, String reason) {
        String sql = "INSERT INTO medicine_interactions(medicine_a_id, medicine_b_id, risk_level, reason) " +
                     "VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medA);
            ps.setInt(2, medB);
            ps.setString(3, risk);
            ps.setString(4, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("interaction insert failed", e);
        }
    }

    public void delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM medicine_interactions WHERE interaction_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("interaction delete failed", e);
        }
    }

    private Rule map(ResultSet rs) throws SQLException {
        Rule r = new Rule();
        r.id = rs.getInt("interaction_id");
        r.medA = rs.getString("an");
        r.medB = rs.getString("bn");
        r.risk = rs.getString("risk_level");
        r.reason = rs.getString("reason");
        return r;
    }
}
