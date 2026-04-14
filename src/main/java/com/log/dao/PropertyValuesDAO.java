package com.log.dao;

import com.log.model.PropertyValue;

import java.sql.*;

public class PropertyValuesDAO {


    public int insertValue(Connection conn, PropertyValue propertyValue) {

        String sql = "INSERT into Property_Values (Prop_VAL, Property_ID) VALUES (?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setDouble(1, propertyValue.getPropertyVAL());
            stmt.setInt(2, propertyValue.getPropertyID());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void updatePropertyValue(Connection conn, PropertyValue propertyValue) {

        String sql = "UPDATE Property_Values SET Prop_VAL = ?, Property_ID = ? WHERE Property_Value_ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, propertyValue.getPropertyVAL());
            stmt.setInt(2, propertyValue.getPropertyID());
            stmt.setInt(3, propertyValue.getPropertyValID());

            int rowsAffected = stmt.executeUpdate();

            System.out.println(
                    (rowsAffected > 0)
                            ? "Updated property value"
                            : "Failed to update property value"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
