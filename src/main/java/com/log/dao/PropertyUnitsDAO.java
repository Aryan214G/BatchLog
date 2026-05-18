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
}
