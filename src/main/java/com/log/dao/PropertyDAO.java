package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Property;
import com.log.model.PropertyView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    public void insertProperty(Property property) {
        String sql = """
                INSERT INTO Property
                (Property_name, Category_ID, Temp_ID, Dir_ID, Unit_ID, Batch_CODE)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, property.getPropertyName());
            statement.setInt(2, property.getCategoryID());
            statement.setInt(3, property.getTempID());
            statement.setInt(4, property.getDirID());
            statement.setInt(5, property.getUnitID());
            statement.setInt(6, property.getBatchCode());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PropertyView> getPropertiesByBatch(int batchCode) throws SQLException {
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

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    void updateProperty(Property property){
    }

    void deleteProperty(int propertyId){

    }
}
