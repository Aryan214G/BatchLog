package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
