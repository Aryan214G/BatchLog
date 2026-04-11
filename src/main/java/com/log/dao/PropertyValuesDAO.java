package com.log.dao;

import com.log.model.PropertyValue;

import java.sql.*;

public class PropertyValuesDAO {


    public int insertValue(Connection conn, PropertyValue propertyValue) {

        String sql = "INSERT into Property_Values (Prop_VAL, Property_ID) VALUES (?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, propertyValue.getPropertyVAL());
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
}
