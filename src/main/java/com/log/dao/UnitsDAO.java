package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitsDAO {

    public void insertUnit(Unit unit) {

        String sql = "INSERT INTO Units (Unit_ID, Unit) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unit.getUnitId());
            stmt.setString(2, unit.getUnit());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Unit getUnit(int unitId) {

        String sql = "SELECT * FROM Units WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unitId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Unit(
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Unit> getAllUnits() {

        List<Unit> units = new ArrayList<>();

        String sql = "SELECT * FROM Units";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Unit unit = new Unit(
                        rs.getInt("Unit_ID"),
                        rs.getString("Unit")
                );

                units.add(unit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }


    public void updateUnit(Unit unit) {

        String sql = "UPDATE Units SET Unit = ? WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, unit.getUnit());
            stmt.setInt(2, unit.getUnitId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteUnit(int unitId) {

        String sql = "DELETE FROM Units WHERE Unit_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unitId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}