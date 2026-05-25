package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  PropertyDAO {

    public int insertProperty(Connection conn, Property property) {
        String sql = """
                INSERT INTO Property
                (Property_name, Category_ID, Temp_ID, Dir_ID, Unit_ID, Test_ID)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, property.getPropertyName());
            statement.setInt(2, property.getCategory().getCategoryId());
            statement.setInt(3, property.getTemperature().getTempId());
            statement.setInt(4, property.getDirection().getDirId());
            statement.setInt(5, property.getUnit().getUnitId());
            statement.setInt(6, property.getTestID());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public List<Property> getPropertiesByTest(int testId) throws SQLException {

    List<Property> properties = new ArrayList<>();

    String sql = """
        SELECT
            p.Property_ID,
            p.Property_name,

            p.Category_ID,
            c.Category_name,

            p.Temp_ID,
            t.Temp_VAL,
            t.Temp_Unit_ID,
            tu.Temp_Unit AS Temp_Unit,

            p.Dir_ID,
            d.Dir_VAL,

            p.Unit_ID,
            u.Unit AS Property_Unit,

            p.Test_ID

        FROM Property p

        JOIN Category c
            ON p.Category_ID = c.Category_ID

        JOIN Units u
            ON p.Unit_ID = u.Unit_ID

        JOIN Direction d
            ON p.Dir_ID = d.Dir_ID

        JOIN Temperature t
            ON p.Temp_ID = t.Temp_ID

        JOIN Temperature_Units tu
            ON t.Temp_Unit_ID = tu.Temp_Unit_ID

        WHERE p.Test_ID = ?
        """;

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, testId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Property property = new Property();

            property.setPropertyID(rs.getInt("Property_ID"));
            property.setPropertyName(rs.getString("Property_name"));

            property.getCategory().setCategoryId(rs.getInt("Category_ID"));
            property.getCategory().setCategoryName(rs.getString("Category_name"));

            property.getTemperature().setTempId(rs.getInt("Temp_ID"));
            property.getTemperature().setTempVal(rs.getDouble("Temp_VAL"));
            property.getTemperature().setTempUnitVal(rs.getString("Temp_Unit"));
            property.getTemperature().setTempUnitId(rs.getInt("Temp_Unit_ID"));

            property.getDirection().setDirId(rs.getInt("Dir_ID"));
            property.getDirection().setDirVal(rs.getString("Dir_VAL"));

            property.getUnit().setUnitId(rs.getInt("Unit_ID"));
            property.getUnit().setUnit(rs.getString("Property_Unit"));

            property.setTestID(rs.getInt("Test_ID"));

            properties.add(property);
        }

    } catch (SQLException e) {
        throw new SQLException(
                "Failed to fetch properties for Test " + testId,
                e
        );
    }

    return properties;
}

    public void updateProperty(Connection conn, Property property) {

        String sql = """
            UPDATE Property
            SET Property_name = ?, 
                Category_ID = ?, 
                Temp_ID = ?, 
                Dir_ID = ?, 
                Unit_ID = ?, 
                Test_ID = ?
            WHERE Property_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategory().getCategoryId());
            stmt.setInt(3, property.getTemperature().getTempId());
            stmt.setInt(4, property.getDirection().getDirId());
            stmt.setInt(5, property.getUnit().getUnitId());
            stmt.setInt(6, property.getTestID());
            stmt.setInt(7, property.getPropertyID());

            int rowsAffected = stmt.executeUpdate();

            System.out.println(
                    (rowsAffected > 0)
                            ? "Updated property"
                            : "Failed to update property"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteProperty(int propertyId){

    }

    public int getPropertyId(Connection conn, Property property) {

        String sql = """
            SELECT Property_ID
            FROM Property
            WHERE Property_name = ?
              AND Category_ID = ?
              AND Test_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategory().getCategoryId());
            stmt.setInt(3, property.getTestID());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Property_ID"); // found
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }
}
