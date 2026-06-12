package com.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PropertyUnitsDAO {

    public List<String> getUnitsByProperty(Connection conn, int defPropId) {

    List<String> units = new ArrayList<>();

    String sql = """
        SELECT u.Unit
        FROM Property_Units pu
        JOIN Units u
            ON pu.Unit_ID = u.Unit_ID
        WHERE pu.Def_PropID = ?
    """;

    try (PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setInt(1, defPropId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            units.add(rs.getString("Unit"));
        }

    } catch (SQLException e) {

        throw new RuntimeException(e);
    }

    return units;
}

    public void addUnitToProperty(
            Connection conn,
            int defPropId,
            int unitId
    ) {

        String sql = """
        INSERT INTO Property_Units
        (
            Def_PropID,
            Unit_ID
        )
        VALUES (?, ?)
        """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, defPropId);
            stmt.setInt(2, unitId);

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public void removeUnitFromProperty(
            Connection conn,
            int defPropId,
            int unitId
    ) {

        String sql = """
        DELETE FROM Property_Units
        WHERE Def_PropID = ?
        AND Unit_ID = ?
        """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, defPropId);
            stmt.setInt(2, unitId);

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public boolean unitExistsForProperty(
            Connection conn,
            int defPropId,
            int unitId
    ) {

        String sql = """
        SELECT 1
        FROM Property_Units
        WHERE Def_PropID = ?
        AND Unit_ID = ?
        """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, defPropId);
            stmt.setInt(2, unitId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
