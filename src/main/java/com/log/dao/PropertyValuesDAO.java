package com.log.dao;

import com.log.model.PropertyValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PropertyValuesDAO {
    public void insertValue(Connection conn, PropertyValue propertyValue) {

        String sql = "INSERT into Property_Values (Prop_VAL, Property_ID) VALUES (?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, propertyValue.getPropertyVAL());
            stmt.setInt(2, propertyValue.getPropertyID());
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
