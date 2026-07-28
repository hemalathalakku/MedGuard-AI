package com.medguard.dao;

import com.medguard.exception.DataAccessException;
import com.medguard.model.Medicine;
import com.medguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for the "medicines" table. */
public class MedicineDAO {

    /** Search by medicine, brand OR generic name, paged (page starts at 1). */
    public List<Medicine> search(String term, int page, int pageSize) {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE medicine_name LIKE ? OR brand LIKE ? " +
                     "OR generic_name LIKE ? ORDER BY medicine_name LIMIT ? OFFSET ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + (term == null ? "" : term) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setInt(4, pageSize);
            ps.setInt(5, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("medicine search failed", e);
        }
        return list;
    }

    public int countSearch(String term) {
        String sql = "SELECT COUNT(*) FROM medicines WHERE medicine_name LIKE ? OR brand LIKE ? OR generic_name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + (term == null ? "" : term) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("medicine countSearch failed", e);
        }
    }

    public Medicine findById(int id) {
        String sql = "SELECT * FROM medicines WHERE medicine_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("medicine findById failed", e);
        }
    }

    public void insert(Medicine m) { upsert(m, false); }
    public void update(Medicine m) { upsert(m, true); }

    /** Shared insert/update to avoid duplicated column lists. */
    private void upsert(Medicine m, boolean isUpdate) {
        String cols = "medicine_name=?, generic_name=?, brand=?, manufacturer=?, ingredients=?, " +
                      "description=?, uses=?, dosage=?, side_effects=?, contraindications=?, " +
                      "warnings=?, pregnancy_category=?, storage_instructions=?, age_restrictions=?";
        String sql = isUpdate ? "UPDATE medicines SET " + cols + " WHERE medicine_id=?"
                              : "INSERT INTO medicines SET " + cols;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getMedicineName());
            ps.setString(2, m.getGenericName());
            ps.setString(3, m.getBrand());
            ps.setString(4, m.getManufacturer());
            ps.setString(5, m.getIngredients());
            ps.setString(6, m.getDescription());
            ps.setString(7, m.getUses());
            ps.setString(8, m.getDosage());
            ps.setString(9, m.getSideEffects());
            ps.setString(10, m.getContraindications());
            ps.setString(11, m.getWarnings());
            ps.setString(12, m.getPregnancyCategory());
            ps.setString(13, m.getStorageInstructions());
            ps.setString(14, m.getAgeRestrictions());
            if (isUpdate) ps.setInt(15, m.getMedicineId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("medicine save failed", e);
        }
    }

    public void delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM medicines WHERE medicine_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("medicine delete failed", e);
        }
    }

    public int count() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM medicines")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("medicine count failed", e);
        }
    }

    /** Lightweight name list for the search autocomplete (returns "id:name" pairs). */
    public List<String[]> namesLike(String term, int limit) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT medicine_id, medicine_name FROM medicines WHERE medicine_name LIKE ? " +
                     "ORDER BY medicine_name LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + term + "%");
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(new String[]{String.valueOf(rs.getInt(1)), rs.getString(2)});
            }
        } catch (SQLException e) {
            throw new DataAccessException("namesLike failed", e);
        }
        return list;
    }

    private Medicine map(ResultSet rs) throws SQLException {
        Medicine m = new Medicine();
        m.setMedicineId(rs.getInt("medicine_id"));
        m.setMedicineName(rs.getString("medicine_name"));
        m.setGenericName(rs.getString("generic_name"));
        m.setBrand(rs.getString("brand"));
        m.setManufacturer(rs.getString("manufacturer"));
        m.setIngredients(rs.getString("ingredients"));
        m.setDescription(rs.getString("description"));
        m.setUses(rs.getString("uses"));
        m.setDosage(rs.getString("dosage"));
        m.setSideEffects(rs.getString("side_effects"));
        m.setContraindications(rs.getString("contraindications"));
        m.setWarnings(rs.getString("warnings"));
        m.setPregnancyCategory(rs.getString("pregnancy_category"));
        m.setStorageInstructions(rs.getString("storage_instructions"));
        m.setAgeRestrictions(rs.getString("age_restrictions"));
        return m;
    }
}
