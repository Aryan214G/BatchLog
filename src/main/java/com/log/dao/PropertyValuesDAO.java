package com.log.dao;

import com.log.model.PropertyValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        String sql = "UPDATE Property_Values SET Prop_VAL = ?, Property_ID = ? WHERE Prop_VAL_ID = ?";

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

    public int getPropertyValueId(Connection conn, int propertyId) {

        String sql = """
            SELECT Prop_VAL_ID
            FROM Property_Values
            WHERE Property_ID = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, propertyId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Prop_VAL_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

    public List<PropertyValue> getValuesByProperty(Connection conn, int propertyId) {

        List<PropertyValue> list = new ArrayList<>();

        String sql = """
        SELECT Prop_VAL_ID, Prop_VAL
        FROM Property_Values
        WHERE Property_ID = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, propertyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PropertyValue val = new PropertyValue(
                        rs.getInt("Prop_VAL_ID"),
                        rs.getDouble("Prop_VAL"),
                        propertyId
                );

                list.add(val);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deletePropertyValue(Connection conn, int propertyValId) {

        String sql = """
                DELETE FROM Property_Values
                WHERE Prop_VAL_ID = ?
                """;

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, propertyValId);

            int rowsAffected = stmt.executeUpdate();

            System.out.println(
                    (rowsAffected > 0)
                            ? "Deleted property value"
                            : "Property value not found"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


