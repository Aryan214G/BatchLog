package com.log.dao;

import com.log.model.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {


    // =========================================
    // Insert Temperature Unit
    // =========================================

    public int insertTemperatureUnit(
            Connection conn,
            Unit unit
    ) {

        String sql = """
                INSERT INTO Temperature_Units
                (Temp_Unit)
                VALUES (?)
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            stmt.setString(1, unit.getUnit());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    // =========================================
    // Get by ID
    // =========================================

    public Unit getTemperatureUnitById(
            Connection conn,
            int tempUnitId
    ) {

        String sql = """
                SELECT *
                FROM Temperature_Units
                WHERE Temp_Unit_ID = ?
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, tempUnitId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Unit unit = new Unit();

                unit.setUnitId(
                        rs.getInt("Temp_Unit_ID")
                );

                unit.setUnit(
                        rs.getString("Temp_Unit")
                );

                return unit;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================================
    // Get by Unit String
    // =========================================

    public Unit getTemperatureUnit(
            Connection conn,
            String tempUnit
    ) {

        String sql = """
                SELECT *
                FROM Temperature_Units
                WHERE Temp_Unit = ?
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, tempUnit);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Unit unit = new Unit();

                unit.setUnitId(
                        rs.getInt("Temp_Unit_ID")
                );

                unit.setUnit(
                        rs.getString("Temp_Unit")
                );

                return unit;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

        public List<String> getAllTemperatureUnits(Connection conn) {

        List<String> units = new ArrayList<>();

        String sql = """
                SELECT Temp_Unit
                FROM Temperature_Units
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                units.add(rs.getString("Temp_Unit"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }

}