package com.log.dao;

import com.log.model.Property;
import com.log.model.PropertyView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  PropertyDAO {

    public int insertProperty(Connection conn, Property property) {
        String sql = """
                INSERT INTO Property
                (Property_name, Category_ID, Temp_ID, Dir_ID, Unit_ID, Batch_CODE)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, property.getPropertyName());
            statement.setInt(2, property.getCategoryID());
            statement.setInt(3, property.getTempID());
            statement.setInt(4, property.getDirID());
            statement.setInt(5, property.getUnitID());
            statement.setInt(6, property.getBatchCode());

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


    public List<PropertyView> getPropertiesByBatch(Connection conn, int batchCode) throws SQLException {
        List<PropertyView> properties = new ArrayList<>();

        String sql = """
                SELECT
                    p.Property_ID,
                    p.Property_name,
                    c.Category_name,
                    t.Temp_VAL,
                    d.Dir_VAL,
                    u.Unit
                FROM Property p
                JOIN Category c ON p.Category_ID = c.Category_ID
                JOIN Units u ON p.Unit_ID = u.Unit_ID
                JOIN Direction d ON p.Dir_ID = d.Dir_ID
                JOIN Temperature t ON p.Temp_ID = t.Temp_ID
                WHERE p.Batch_CODE = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batchCode);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                    int id = rs.getInt("Property_ID");
                    String propertyName = rs.getString("Property_name");
                    String categoryName = rs.getString("Category_name");
                    double tempValue = rs.getDouble("Temp_VAL");
                    String direction = rs.getString("Dir_VAL");
                    String propertyUnit = rs.getString("Unit");

                    properties.add(new PropertyView(id, propertyName, categoryName, tempValue,
                            direction, propertyUnit));

            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch properties for batch " + batchCode, e);
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
                Batch_CODE = ?
            WHERE Property_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategoryID());
            stmt.setInt(3, property.getTempID());
            stmt.setInt(4, property.getDirID());
            stmt.setInt(5, property.getUnitID());
            stmt.setInt(6, property.getBatchCode());
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
              AND Batch_CODE = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getPropertyName());
            stmt.setInt(2, property.getCategoryID());
            stmt.setInt(3, property.getBatchCode());

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
